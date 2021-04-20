package com.knoldus.trade.reporting.service

import com.crankuptheamps.client.HAClient
import com.knoldus.common.amps.AmpsMessageHandler
import com.knoldus.common.model.{TradeReportRequest, TradeStatus}
import com.knoldus.common.persistence.CassandraOperation
import com.knoldus.common.util.JsonHelper._
import com.knoldus.trade.reporting.util.ConfigUtil.INPUT_TOPIC_NAME
import spray.json._

import scala.util.{Failure, Success}

object TradeReportService {
  def apply(hAClient: HAClient): Unit = {
    val ampsHandler = new AmpsMessageHandler(hAClient)
    val subscriber = ampsHandler.subscribe(INPUT_TOPIC_NAME)

    subscriber match {
      case Success(messageStream) => messageStream.foreach { message =>
        val tradeReportRequest = message.getData.parseJson.convertTo[TradeReportRequest].copy(tradeStatus = TradeStatus.Cleared)
        println("Trader Reporting ::: " + tradeReportRequest)
        CassandraOperation.insertTradeReport(tradeReportRequest.toJson.toString())
      }
      case Failure(error) => println(error.getMessage)
    }
  }
}
