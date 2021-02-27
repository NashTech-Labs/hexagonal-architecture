package com.knoldus.trading.view.service

import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.ActorContext
import akka.http.scaladsl.server.Directives.{as, entity, post, _}
import akka.http.scaladsl.server.Route
import com.knoldus.common.command.{CreateNewOrder, ExternalCommand}
import com.knoldus.common.model.OrderRequest.Order

class OrderApi(ctx: ActorSystem) {
  val routes: Route = postOrder ~ getAllOrder ~ getOrder
  import com.knoldus.common.util.JsonHelper._
  def postOrder: Route = path("orders") {
    post {
      entity(as[Order]) { value =>
        val event = CreateNewOrder(value)
        println(s"@ @@@ ${ctx.name}")
        ctx.eventStream.publish(event)
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
