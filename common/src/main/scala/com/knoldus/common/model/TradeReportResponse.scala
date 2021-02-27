package com.knoldus.common.model

object TradeReportResponse {
  case class TradeOrderInfo(orderId: String, side: String, price: Double, quantity: Int,
                            productCode: Int, productType: String, timeStamp: Long)
  case class TradeResponse(tradeId: String, price: Double, volume: Int, tradeOrderInfo: List[TradeOrderInfo],
                         tradeStatus: String)
}
