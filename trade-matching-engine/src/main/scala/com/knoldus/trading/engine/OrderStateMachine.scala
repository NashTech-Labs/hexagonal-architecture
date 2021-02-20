package com.knoldus.trading.engine

import com.knoldus.common.command.{Command, CreateNewOrder}
import com.knoldus.common.event.Event
import com.knoldus.trading.event.{OrderCreated, OrderInternalEvent}
import com.knoldus.trading.model.OrderModel.Order
import com.knoldus.trading.state.{Final, NonFinal, OrderState, OrderStatus}

object OrderStateMachine {

  def newOrder(order: Order): OrderState = New(order)

  // states
  case class New(order: Order) extends OrderState with NonFinal {

    override def handleCommand: PartialFunction[Command, OrderInternalEvent] = {
      case cmd: CreateNewOrder =>
        OrderCreated(order.copy(orderStatus = OrderStatus.New))
    }

    override def onEvent: PartialFunction[Event, OrderState] = {
      case evt: OrderCreated => copy(order = evt.order)
    }
  }

  /*case class Matched(order: Order) extends OrderState with NonFinal {

    override def handleCommand: PartialFunction[Command, OrderInternalEvent] = {
      case cmd: CreateNewOrder =>
        OrderCreated(order.copy(orderStatus = OrderStatus.New))
    }

    override def onEvent: PartialFunction[Event, OrderState] = {
      case evt: OrderCreated => copy(order = evt.order)
    }
  }

  case class Booked(order: Order) extends OrderState with Final {

    override def handleCommand: PartialFunction[Command, OrderInternalEvent] = {
      case cmd: CreateNewOrder =>
        OrderCreated(order.copy(orderStatus = OrderStatus.New))
    }

    override def onEvent: PartialFunction[Event, OrderState] = {
      case evt: OrderCreated => copy(order = evt.order)
    }
  }*/

}
