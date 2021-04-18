package com.knoldus.trade.reporting.util

import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._

object ConfigUtil {
  val config = ConfigFactory.load()
  val INPUT_TOPIC_NAME = config.getString("amps.input_topic")
  val SERVER_IPS = config.getStringList("amps.servers").asScala.toList
  val CLIENT_NAME = config.getString("amps_client.client_name")
}
