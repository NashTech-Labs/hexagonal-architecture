package com.knoldus.trading.view.handler

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.knoldus.common.event.{BookedOrderResponse, ExternalEvent, MatchedOrderResponse, NewOrderCreated}
import com.knoldus.common.model.OrderResponse.OrderCreatedResponse
import com.knoldus.common.persistence.model.PersistenceOrder
import com.knoldus.common.persistence.repository.OrderRepository

object ViewOutputHandler {

  def apply(orderRepository: OrderRepository): Behavior[ExternalEvent] = Behaviors.setup { ctx =>
    Behaviors.receiveMessage {
      case evt: NewOrderCreated =>
        ctx.log.info(s"NewOrderCreated::::: ${evt}")
        orderRepository.insertOrder(getPersistenceOrder(evt.orderResponse))
        Behaviors.same
      case evt: MatchedOrderResponse =>
        ctx.log.info(s"MatchedOrder::::: ${evt}")
        orderRepository.insertOrder(getPersistenceOrder(evt.orderResponse))
        Behaviors.same
      case evt: BookedOrderResponse =>
        ctx.log.info(s"BookedOrder::::: ${evt}")
        orderRepository.insertOrder(getPersistenceOrder(evt.orderResponse))
        Behaviors.same
      case _ => Behaviors.same
    }
  }

  def getPersistenceOrder(order: OrderCreatedResponse): PersistenceOrder = PersistenceOrder(order.orderId, order.side, order.price, order.quantity,
    order.productCode, order.productType, order.timeStamp, order.status, order.source)

}
