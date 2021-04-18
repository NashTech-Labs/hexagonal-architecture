package com.knoldus.booking.service

import java.util.concurrent.atomic.AtomicInteger

import com.knoldus.booking.util.ConfigUtil._
import com.knoldus.common.amps.{AmpsClient, AmpsMessageHandler}
import com.knoldus.common.event.BookingOrderRequest
import com.knoldus.common.model.{TradeReportRequest, TradeStatus}
import com.knoldus.common.util.JsonHelper._
import spray.json._

import scala.util.{Failure, Success}

object TradeBookingService extends App {


  val hAClient = AmpsClient.getConnection(CLIENT_NAME, SERVER_IPS)
  val ampsHandler = new AmpsMessageHandler(hAClient)
  val subscriber = ampsHandler.subscribe(INPUT_TOPIC_NAME)
  private val tradeId: AtomicInteger = new AtomicInteger(1)

  subscriber match {
    case Success(messageStream) => messageStream.foreach { message =>
      val bookingRequest = message.getData.parseJson.convertTo[BookingOrderRequest]
      val tradeReportRequest = getTradeReportRequest(tradeId.getAndIncrement().toString, bookingRequest)
      println(tradeReportRequest)
      publishMessage(tradeReportRequest.toJson.toString())
    }
    case Failure(error) => println(error.getMessage)
  }

  private def publishMessage(message: String): Unit = {
    ampsHandler.publish(OUTPUT_TOPIC_NAME, message) match {
      case Success(sequenceNumber) => println(s"Sequence Number : $sequenceNumber")
      case Failure(error) => println(error.getMessage)
    }
  }

  private def getTradeReportRequest(tradeId: String, bookingReq: BookingOrderRequest): TradeReportRequest = {
    TradeReportRequest(tradeId, bookingReq.price, bookingReq.volume,
      bookingReq.productCode, bookingReq.productType, System.currentTimeMillis(),
      bookingReq.orderIds, TradeStatus.Booked)
  }
}
