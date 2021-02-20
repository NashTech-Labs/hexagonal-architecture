package com.knoldus.common.event
import com.knoldus.common.model.OrderResponse._
trait Event
trait ExternalEvent extends Event
final case class OrderCreated(orderResponse: OrderCreatedResponse) extends ExternalEvent