package com.knoldus.trading.event

import com.knoldus.common.event.Event
import com.knoldus.trading.model.OrderModel.Order

trait InternalEvent extends Event
trait OrderInternalEvent extends InternalEvent
final case class OrderCreated(order: Order) extends OrderInternalEvent