package com.knoldus.common.model

object OrderResponse {
case class OrderCreatedResponse(orderId: String, side: String, price: Double, quantity: Int, productCode: Int, productType: String, status: String)
}
