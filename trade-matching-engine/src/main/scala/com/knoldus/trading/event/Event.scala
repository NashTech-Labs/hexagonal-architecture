package com.knoldus.trading.event

import com.knoldus.common.event.Event
import com.knoldus.trading.model.OrderModel.Order

trait InternalEvent extends Event

trait OrderInternalEvent extends InternalEvent

final case class OrderCreated(order: Order) extends OrderInternalEvent
final case class MatchedOrder(order: Order, oppositeOrderId: String) extends OrderInternalEvent
final case class OrderBooked(order: Order, oppositeOrderId: String) extends OrderInternalEvent

