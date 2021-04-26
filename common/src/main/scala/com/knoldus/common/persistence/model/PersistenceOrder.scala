package com.knoldus.common.persistence.model

case class PersistenceOrder(orderId: String, side: String, price: Double, quantity: Int,
                            productCode: Int, productType: String, timeStamp: Long, status: String, source: String)
