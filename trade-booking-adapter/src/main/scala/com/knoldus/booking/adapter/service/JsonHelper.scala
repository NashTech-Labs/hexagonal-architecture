package com.knoldus.booking.adapter.service

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.knoldus.booking.adapter.model.TradeBookingModel.TradeReport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object JsonHelper extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val tradeReport: RootJsonFormat[TradeReport] = jsonFormat8(TradeReport)
}