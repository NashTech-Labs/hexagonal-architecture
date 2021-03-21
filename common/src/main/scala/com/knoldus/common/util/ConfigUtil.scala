package com.knoldus.common.util

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._

object ConfigUtil {

  val config: Config = ConfigFactory.load()

  val port: Int = config.getInt("cassandra.port")
  val hosts: List[String] = config.getStringList("cassandra.hosts").asScala.toList
  val cassandraKeyspaces = config.getStringList("cassandra.keyspaces")
  val replicationFactor: Int = config.getString("cassandra.replication_factor").toInt
  val readConsistency: String = config.getString("cassandra.read_consistency")
  val writeConsistency: String = config.getString("cassandra.write_consistency")

}
