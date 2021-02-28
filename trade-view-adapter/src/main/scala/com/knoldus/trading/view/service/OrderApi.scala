package com.knoldus.trading.view.service

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{as, entity, post, _}
import akka.http.scaladsl.server.Route
import com.knoldus.common.command.CreateNewOrder
import com.knoldus.common.model.OrderRequest.Order
import com.knoldus.common.persistence.CassandraOperation
import com.knoldus.common.persistence.repository.OrderRepository

import scala.concurrent.ExecutionContext

class OrderApi(ctx: ActorSystem, orderRepository: OrderRepository) {
  implicit val ex: ExecutionContext = ctx.dispatcher
  val routes: Route = postOrder ~ getAllOrder ~ getOrder ~ getAllTraderReport ~ getTradeReport

  import com.knoldus.common.util.JsonHelper._

  def postOrder: Route = path("orders") {
    post {
      entity(as[Order]) { value =>
        val event = CreateNewOrder(value)
        ctx.eventStream.publish(event)
        complete(value.toString)
      }
    }
  }

  def getAllOrder: Route = path("orders") {
    get {
      onSuccess(orderRepository.getAllOrders) { orders =>
        complete(orders.toList)
      }
    }
  }

  def getOrder: Route = path("orders" / Segment) { id: String =>
    get {
      onSuccess(orderRepository.getOrderById(id)) { order =>
        println(s" order response ${order}")
        complete(order)
      }
    }
  }

  def getAllTraderReport: Route = path("trade_reports") {
    get {
      complete(CassandraOperation.findAllTradeReport)
    }
  }

  def getTradeReport: Route = path("trade_report" / Segment) { id: String =>
    get {
      complete(CassandraOperation.findTradeReportById(id))
    }
  }
}
