package com.knoldus.trading.view.service

import akka.http.scaladsl.server.Directives.{as, entity, post, _}
import akka.http.scaladsl.server.Route
import com.knoldus.common.model.OrderRequest.Order

class OrderApi {
  val routes: Route = postOrder ~ getAllOrder ~ getOrder

  import com.knoldus.trading.view.util.JsonHelper._

  def postOrder: Route = path("orders") {
    post {
      entity(as[Order]) { value =>
        complete(value.toString)
      }
    }
  }

  def getAllOrder: Route = path("orders") {
    get {
      entity(as[String]) { value =>
        complete(value)
      }
    }
  }

  def getOrder: Route = path("orders" / Segment) { id: String =>
    get {
      entity(as[String]) { value =>
        complete(value)
      }
    }
  }
}
