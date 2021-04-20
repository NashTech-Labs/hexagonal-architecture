package com.knoldus.trade.reporting.service

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.knoldus.common.persistence.CassandraOperation

class TradeReportingApi {

  val routes: Route = getAllTraderReport ~ getTradeReport

  import com.knoldus.common.util.JsonHelper._

  def getAllTraderReport: Route = path("trade_reports") {
    get {
      complete(CassandraOperation.findAllTradeReport)
    }
  }

  def getTradeReport: Route = path("trade_report" / Segment) { id: String =>
    get {
      complete(CassandraOperation.findTradeReportById(id))
    }
  }
}
