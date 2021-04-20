package com.knoldus.common.model

object OrderRequest {

  //TODO: Make quantity of type double
  case class Order(side: String, price: Double, quantity: Int, productCode: Int, productType: String)

}
