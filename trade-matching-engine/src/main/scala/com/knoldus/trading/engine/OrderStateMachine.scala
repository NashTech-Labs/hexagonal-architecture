package com.knoldus.trading.engine

import com.knoldus.common.command.{Command, CreateNewOrder}
import com.knoldus.common.event.{Event, ExternalEvent, MatchedOrderResponse, NewOrderCreated}
import com.knoldus.common.model.OrderResponse.OrderCreatedResponse
import com.knoldus.trading.command.OrderMatched
import com.knoldus.trading.event.{MatchedOrder, OrderCreated, OrderInternalEvent}
import com.knoldus.trading.model.OrderModel.Order
import com.knoldus.trading.state.{FinalState, NonFinalState, OrderState, OrderStatus}

object OrderStateMachine {

  def newOrder(order: Order): OrderState = New(order)

  // states
  case class New(order: Order) extends OrderState with NonFinalState {

    override def handleCommand: PartialFunction[Command, (OrderInternalEvent, ExternalEvent)] = {
      case _: CreateNewOrder =>
        val orderResponse = OrderCreatedResponse(order.orderId, order.side, order.price, order.quantity,
        order.productCode, order.productType, order.timeStamp, OrderStatus.New)
        val externalEvent = NewOrderCreated(orderResponse)
        (OrderCreated(order.copy(orderStatus = OrderStatus.New)), externalEvent)
      case cmd: OrderMatched =>
        val orderResponse = OrderCreatedResponse(order.orderId, order.side, order.price, order.quantity,
          order.productCode, order.productType, order.timeStamp, OrderStatus.Matched)
        (MatchedOrder(order, cmd.matchedWithOrderId), MatchedOrderResponse(orderResponse, cmd.matchedWithOrderId))
    }

    override def onEvent: PartialFunction[Event, OrderState] = {
      case evt: OrderCreated => copy(order = evt.order)
      case evt: MatchedOrder => Matched(order.copy(orderStatus = OrderStatus.Matched), evt.oppositeOrderId)
    }
  }

  case class Matched(order: Order, oppositeOrderId: String) extends OrderState with NonFinalState {

    override def handleCommand: PartialFunction[Command, (OrderInternalEvent, ExternalEvent)] = {
      case cmd: CreateNewOrder => ???
    }

    override def onEvent: PartialFunction[Event, OrderState] = {
      case evt: OrderCreated => copy(order = evt.order)
    }
  }

  /*case class Booked(order: Order) extends OrderState with Final {

    override def handleCommand: PartialFunction[Command, OrderInternalEvent] = {
      case cmd: CreateNewOrder =>
        OrderCreated(order.copy(orderStatus = OrderStatus.New))
    }

    override def onEvent: PartialFunction[Event, OrderState] = {
      case evt: OrderCreated => copy(order = evt.order)
    }
  }*/

}
