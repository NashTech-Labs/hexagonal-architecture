package com.knoldus.common.event

import com.knoldus.common.model.OrderResponse._

trait Event

trait ExternalEvent extends Event

final case class NewOrderCreated(orderResponse: OrderCreatedResponse) extends ExternalEvent

final case class MatchedOrderResponse(orderResponse: OrderCreatedResponse, oppositeOrderId: String) extends ExternalEvent

final case class BookingOrderRequest(price: Double, volume: Int, productCode: Int, productType: String,
                                     orderIds: List[String]) extends ExternalEvent

final case class BookedOrderResponse(orderResponse: OrderCreatedResponse) extends ExternalEvent
