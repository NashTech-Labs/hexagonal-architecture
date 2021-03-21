package com.knoldus.booking.adapter.model

import com.knoldus.booking.adapter.model.TradeStatus.TradeStatus

object TradeStatus extends Enumeration {
  type TradeStatus = String
  val Matched = "Matched"
  val Booked = "Booked"
  val Cleared = "Cleared"

}

object TradeBookingModel {

  case class TradeReport(tradeId: String, price: Double, volume: Int,
                         productCode: Int, productType: String, timeStamp: Long,
                         matchedOrderIds: List[String], tradeStatus: TradeStatus)

}
