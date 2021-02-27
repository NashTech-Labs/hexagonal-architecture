package com.knoldus.common.persistence

import com.datastax.driver.core._
import org.slf4j.LoggerFactory

trait CassandraConnection {

  import com.knoldus.common.util.ConfigUtil._

  val logger = LoggerFactory.getLogger(getClass.getName)
  val defaultConsistencyLevel = ConsistencyLevel.valueOf(writeConsistency)
  val cassandraConn: Session = {
    val cluster = new Cluster.Builder().withClusterName("Test Cluster").withoutJMXReporting().
      addContactPoints(hosts.toArray: _*).
      withPort(port).
      withQueryOptions(new QueryOptions().setConsistencyLevel(defaultConsistencyLevel)).build
    val session = cluster.connect
    session.execute(s"CREATE KEYSPACE IF NOT EXISTS  ${cassandraKeyspaces.get(0)} WITH REPLICATION = " +
      s"{ 'class' : 'SimpleStrategy', 'replication_factor' : ${replicationFactor} }")
    session.execute(s"USE ${cassandraKeyspaces.get(0)}")

    val typeQuery = s"CREATE TYPE TradeOrderInfo " +
      s"(orderId text, side text, price double, quantity int, " +
      s"productCode int, productType text, timeStamp bigint) "

    val tableQuery = s"CREATE TABLE IF NOT EXISTS trader_reports " +
      s"(tradeId bigint, price double, volume int, " +
      s"tradeOrderInfo set<TradeOrderInfo>, status text, " +
      s" PRIMARY KEY (tradeId))"

    createTables(session, typeQuery)
    createTables(session, tableQuery)
    session
  }


  def createTables(session: Session, query: String): ResultSet = session.execute(query)

}

object CassandraConnection extends CassandraConnection

