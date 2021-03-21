package com.knoldus.trading.engine

import com.knoldus.common.command.{BookedOrder, Command, CreateNewOrder}
import com.knoldus.common.event._
import com.knoldus.common.model.OrderResponse.OrderCreatedResponse
import com.knoldus.trading.command.OrderMatched
import com.knoldus.trading.event.{MatchedOrder, OrderBooked, OrderCreated, OrderInternalEvent}
import com.knoldus.trading.model.OrderModel.Order
import com.knoldus.trading.state.OrderStatus.OrderStatus
import com.knoldus.trading.state.{FinalState, NonFinalState, OrderState, OrderStatus}

object OrderStateMachine {

  def newOrder(order: Order): OrderState = New(order)

  private def getOrderResponse(order: Order, orderStatus: OrderStatus): OrderCreatedResponse =
    OrderCreatedResponse(order.orderId, order.side, order.price, order.quantity,
      order.productCode, order.productType, order.timeStamp, orderStatus)

  case class New(order: Order) extends OrderState with NonFinalState {

    override def handleCommand: PartialFunction[Command, (OrderInternalEvent, ExternalEvent)] = {
      case _: CreateNewOrder =>
        (OrderCreated(order.copy(orderStatus = OrderStatus.New)),
          NewOrderCreated(getOrderResponse(order, OrderStatus.New)))
      case cmd: OrderMatched =>
        (MatchedOrder(order, cmd.matchedWithOrderId), MatchedOrderResponse(getOrderResponse(order, OrderStatus.Matched),
            cmd.matchedWithOrderId))
    }

    override def onEvent: PartialFunction[Event, OrderState] = {
      case evt: OrderCreated => copy(order = evt.order)
      case evt: MatchedOrder => Matched(order.copy(orderStatus = OrderStatus.Matched), evt.oppositeOrderId)
    }
  }

  case class Matched(order: Order, oppositeOrderId: String) extends OrderState with NonFinalState {

    override def handleCommand: PartialFunction[Command, (OrderInternalEvent, ExternalEvent)] = {
      case _: BookedOrder =>
        (OrderBooked(order, oppositeOrderId), BookedOrderResponse(getOrderResponse(order, OrderStatus.Booked)))
    }

    override def onEvent: PartialFunction[Event, OrderState] = {
      case _: OrderBooked => Booked(order.copy(orderStatus = OrderStatus.Booked), oppositeOrderId)
    }
  }

  case class Booked(order: Order, oppositeOrderId: String) extends OrderState with FinalState
}
