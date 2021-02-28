package com.knoldus.common.persistence

import com.datastax.driver.core.{ResultSet, Row}
import com.knoldus.common.model.TradeReportResponse.TradeResponse

import scala.collection.JavaConverters._

object CassandraOperation extends CassandraConnection {
  val tableName = "trade_reports"

  def insertTradeReport(tradeJson: String): ResultSet = {
    println(s"About to insert : $tradeJson")
    cassandraConn.execute(s"""INSERT INTO trade_data_store.$tableName JSON '${tradeJson.replaceAll("'", "''")}'""")
  }

  def findTradeReportById(tradeId: String): TradeResponse = {
    val result: Row = cassandraConn.execute(
      s"select * from trade_data_store.trade_reports where tradeid = '$tradeId' allow filtering;"
    ).one()
    getTradeReportResponse(result)
  }

  private def getTradeReportResponse(result: Row): TradeResponse = {
    TradeResponse(result.getString("tradeid"), result.getDouble("price"),
      result.getInt("volume"), result.getInt("productcode"),
      result.getString("producttype"), result.getLong("timestamp"),
      result.getList("matchedorderids", classOf[String]).asScala.toList,
      result.getString("tradestatus"))
  }

  def findAllTradeReport: List[TradeResponse] = {
    val results = cassandraConn.execute(
      s"select * from trade_data_store.trade_reports allow filtering;"
    ).all()
    results.asScala.toList.map(getTradeReportResponse)
  }
}
