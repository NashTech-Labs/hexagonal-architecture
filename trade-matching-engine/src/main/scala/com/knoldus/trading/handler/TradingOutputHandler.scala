package com.knoldus.trading.handler

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.crankuptheamps.client.HAClient
import com.knoldus.common.amps.AmpsMessageHandler
import com.knoldus.common.event.{BookingOrderRequest, ExternalEvent}
import com.knoldus.common.util.Constants.BOOKING_REQ_TOPIC
import com.knoldus.common.util.JsonHelper._
import spray.json._
import scala.util.{Failure, Success}

object TradingOutputHandler {
  def apply(haClient: HAClient): Behavior[ExternalEvent] = Behaviors.setup { _ =>
    val ampsHandler: AmpsMessageHandler = new AmpsMessageHandler(haClient)
    Behaviors.receiveMessage {
      case msg: BookingOrderRequest =>
        publishMessage(ampsHandler, msg.toJson.toString())
        Behaviors.same
      case _ => Behaviors.same
    }
  }

  private def publishMessage(ampsHandler: AmpsMessageHandler, message: String): Unit = {
    ampsHandler.publish(BOOKING_REQ_TOPIC, message) match {
      case Success(sequenceNumber) => println(s"Sequence Number : $sequenceNumber")
      case Failure(error) => println(error.getMessage)
    }
  }
}
