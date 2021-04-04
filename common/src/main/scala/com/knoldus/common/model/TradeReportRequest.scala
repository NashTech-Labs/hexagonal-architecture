package com.knoldus.common.model

import com.knoldus.common.model.TradeStatus.TradeStatus

object TradeStatus extends Enumeration {
  type TradeStatus = String
  val Matched = "Matched"
  val Booked = "Booked"
  val Cleared = "Cleared"

}

case class TradeReportRequest(tradeId: String, price: Double, volume: Int,
                              productCode: Int, productType: String, timeStamp: Long,
                              matchedOrderIds: List[String], tradeStatus: TradeStatus)