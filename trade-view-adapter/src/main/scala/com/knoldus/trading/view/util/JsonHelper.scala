package com.knoldus.trading.view.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.knoldus.common.model.OrderRequest.Order
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object JsonHelper extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val orderRequest: RootJsonFormat[Order] = jsonFormat6(Order)
}

