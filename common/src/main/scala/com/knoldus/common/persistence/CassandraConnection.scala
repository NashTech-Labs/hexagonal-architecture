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

    val tableQuery = s"CREATE TABLE IF NOT EXISTS trade_reports " +
      s"(tradeId text, price double, volume int, productCode int, " +
      s"productType text, timeStamp bigint, " +
      s"matchedOrderIds  list<text>, tradeStatus text, " +
      s" PRIMARY KEY (tradeId))"

    createTables(session, tableQuery)
    session
  }


  def createTables(session: Session, query: String): ResultSet = session.execute(query)

}

object CassandraConnection extends CassandraConnection

