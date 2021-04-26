package com.knoldus.trading.model

import com.knoldus.trading.state.OrderStatus.OrderStatus

object OrderModel {

  case class Order(orderId: String,
                   side: String,
                   price: Double,
                   quantity: Int,
                   productCode: Int,
                   productType: String,
                   timeStamp: Long,
                   orderStatus: OrderStatus,
                   source: String)
}
