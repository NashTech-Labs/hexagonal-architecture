package com.knoldus.fix.adapter

import akka.actor.ActorSystem
import com.crankuptheamps.client.{HAClient, Message}
import com.knoldus.common.amps.{AmpsClient, AmpsMessageHandler}
import com.knoldus.fix.adapter.mapper.ModelMapper
import com.knoldus.fix.adapter.parser.FIXMessageParser
import com.knoldus.fix.adapter.utils.ConfigUtil._
import org.slf4j.{Logger, LoggerFactory}
import quickfix.field.{BeginString, SenderCompID, TargetCompID}
import quickfix.fix44.NewOrderSingle
import quickfix.{DataDictionary, DefaultMessageFactory, Session, SessionID}

import scala.util.{Failure, Success, Try}

object FIXMessageProcessor {

  val logger: Logger = LoggerFactory.getLogger("FixAmpsClient")

  val hAClient: HAClient = AmpsClient.getConnection(CLIENT_NAME, SERVER_IPS)
  val ampsHandler = new AmpsMessageHandler(hAClient)
  val subscriber: Try[Iterator[Message]] = ampsHandler.subscribe(INPUT_TOPIC_NAME)
  val sessionId: SessionID = new SessionID(new BeginString("FIXT.4.2"),
    new SenderCompID("sender"),
    new TargetCompID("target"),
    "Session1")
  val session: Session = Session.lookupSession(sessionId)

  def apply(actorSystem: ActorSystem): Unit = {

    subscriber match {
      case Success(messageStream) => messageStream.foreach { message =>

        val dataDictionary = new DataDictionary("FIX42.xml")
        val newSingleOrder: NewOrderSingle = FIXMessageParser.parse(new DefaultMessageFactory(), dataDictionary, message.getData, session)
        val createNewOrderCmd = ModelMapper.mapNewOrderSingleToCreateNewOrder(newSingleOrder)
        actorSystem.eventStream.publish(createNewOrderCmd)
      }
      case Failure(error) => logger.error(error.getMessage)
    }
  }

  private def publishMessage(message: String): Unit = {
    ampsHandler.publish(OUTPUT_TOPIC_NAME, message) match {
      case Success(sequenceNumber) => println(s"Sequence Number : $sequenceNumber")
      case Failure(error) => println(error.getMessage)
    }
  }
}
