package com.knoldus.common.model

object TradeReportResponse {

  case class TradeResponse(tradeId: String, price: Double, volume: Int,
                           productCode: Int, productType: String, timeStamp: Long,
                           matchedOrderIds: List[String], tradeStatus: String)

}
