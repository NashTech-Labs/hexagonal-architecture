package com.knoldus.common.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.knoldus.common.event.BookingOrderRequest
import com.knoldus.common.model.OrderRequest.Order
import com.knoldus.common.model.OrderResponse.OrderCreatedResponse
import com.knoldus.common.model.TradeReportRequest
import com.knoldus.common.model.TradeReportResponse.TradeResponse
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object JsonHelper extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val orderRequest: RootJsonFormat[Order] = jsonFormat6(Order)
  implicit val orderResponse: RootJsonFormat[OrderCreatedResponse] = jsonFormat9(OrderCreatedResponse)
  implicit val tradeResponse: RootJsonFormat[TradeResponse] = jsonFormat8(TradeResponse)
  implicit val tradeReport: RootJsonFormat[TradeReportRequest] = jsonFormat8(TradeReportRequest)
  implicit val bookingOrderRequest: RootJsonFormat[BookingOrderRequest] = jsonFormat5(BookingOrderRequest)
}

