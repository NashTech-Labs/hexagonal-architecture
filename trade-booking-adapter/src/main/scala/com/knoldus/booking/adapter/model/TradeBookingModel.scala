package com.knoldus.booking.adapter.model

import com.knoldus.booking.adapter.model.TradeStatus.TradeStatus

object TradeStatus extends Enumeration {
  type TradeStatus = String
  val Matched = "Matched"
  val Booked  = "Booked"
  val Cleared = "Cleared"

}
object TradeBookingModel {
  case class TradeOrderInfo(orderId: String, side: String, price: Double, quantity: Int,
                            productCode: Int, productType: String, timeStamp: Long)
  case class TradeReport(tradeId: String, price: Double, volume: Int, tradeOrderInfo: List[TradeOrderInfo],
                        tradeStatus: TradeStatus)

}
