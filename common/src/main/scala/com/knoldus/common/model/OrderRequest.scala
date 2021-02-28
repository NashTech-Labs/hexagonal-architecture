package com.knoldus.common.model

object OrderRequest {

  case class Order(side: String, price: Double, quantity: Int, productCode: Int, productType: String)

}
