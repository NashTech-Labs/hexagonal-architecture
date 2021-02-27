package com.knoldus.common.event
import com.knoldus.common.model.OrderRequest.Order
import com.knoldus.common.model.OrderResponse._

trait Event

trait ExternalEvent extends Event

final case class NewOrderCreated(orderResponse: OrderCreatedResponse) extends ExternalEvent

final case class MatchedOrderResponse(orderResponse: OrderCreatedResponse, oppositeOrderId: String) extends ExternalEvent
