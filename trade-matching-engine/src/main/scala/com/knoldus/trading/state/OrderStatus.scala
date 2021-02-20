package com.knoldus.trading.state

object OrderStatus extends Enumeration {
  type OrderStatus = String
  val New     = "New"
  val Matched = "Matched"
  val Booked  = "Booked"

}
