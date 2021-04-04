package com.knoldus.common.amps

import com.crankuptheamps.client.{HAClient, Message}

import scala.collection.JavaConverters.asScalaIteratorConverter
import scala.util.Try

class AmpsMessageHandler(haClient: HAClient) {

  def publish(topicName: String, message: String): Try[Long] = {
    Try {
      println(s"HA Client Info : ${haClient.getConnectionInfo}")
      println(s"Sending message: ${message} to topic : ${topicName}")
      haClient.publish(topicName, message)
    }
  }

  def subscribe(topicName: String): Try[Iterator[Message]] = {
    Try {
      println(s"Amps client subscribing to ${topicName}")
      haClient.subscribe(topicName).iterator().asScala
    }
  }
}
