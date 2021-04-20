package com.knoldus.fix.adapter.utils

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._

object ConfigUtil {

  val config: Config = ConfigFactory.load()
  val INPUT_TOPIC_NAME: String = config.getString("amps.fix.input_topic")
  val OUTPUT_TOPIC_NAME: String = config.getString("amps.fix.output_topic")
  val SERVER_IPS: List[String] = config.getStringList("amps.fix.servers").asScala.toList
  val CLIENT_NAME: String = config.getString("amps.fix.client_name")
}
