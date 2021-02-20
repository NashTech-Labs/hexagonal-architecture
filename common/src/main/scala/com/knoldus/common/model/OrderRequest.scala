package com.knoldus.common.model

object OrderRequest {
  case class Order(orderId: String, side: String, price: Double, quantity: Int, productCode: Int, productType: String)
}
