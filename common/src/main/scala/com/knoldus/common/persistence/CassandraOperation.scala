package com.knoldus.common.persistence

import com.datastax.driver.core.ResultSet

object CassandraOperation extends CassandraConnection {
  val tableName = "trade_reports"

  def insertTradeReport(tradeJson: String): ResultSet = {
    println(s"About to insert : $tradeJson")
    cassandraConn.execute(s"""INSERT INTO $tableName JSON '${tradeJson.replaceAll("'", "''")}'""")
  }

  def findTradeReportById(tradeId: String): Unit = {
    val result = cassandraConn.execute(
      s"select * from trade_data_store.trade_reports where tradeId = $tradeId allow filtering;"
    ).one()
    println(s"trade report is ${result.toString}")
  }

  def findAllTradeReport: Unit = {
    val result = cassandraConn.execute(
      s"select * from trade_data_store.trade_reports allow filtering;"
    ).all()
    println(s"all trade report is ${result.toString}")
  }

}
