package com.knoldus.trading.state

import com.knoldus.common.command.Command
import com.knoldus.common.event.Event
import com.knoldus.trading.event.OrderInternalEvent
import com.knoldus.trading.model.OrderModel.Order

trait OrderState extends State {
def order: Order
  def handleCommand: PartialFunction[Command, OrderInternalEvent] = PartialFunction.empty
  def onEvent: PartialFunction[Event, OrderState] = {
    case _ => this
  }
}
