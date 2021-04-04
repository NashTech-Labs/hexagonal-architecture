package com.knoldus.trading.app

import akka.actor.typed
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import com.knoldus.common.amps.AmpsClient
import com.knoldus.common.command.ExternalCommand
import com.knoldus.common.persistence.repository.{OrderRepository, OrderRepositoryImpl}
import com.knoldus.trading.view.TradingHttpServer
import com.typesafe.config.{Config, ConfigFactory}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.collection.JavaConverters._

object TradingApp extends App {
  val config: Config = ConfigFactory.load()
  val ampsServers= config.getStringList("amps.servers").asScala.toList
  val clientName = config.getString("amps_client.client_name")
  val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("slick")
  val orderRepository: OrderRepository = new OrderRepositoryImpl(dbConfig)
  val haClient = AmpsClient.getConnection(clientName, ampsServers)

  val behavior = Behaviors.setup[ExternalCommand] { ctx: ActorContext[ExternalCommand] =>
    BindingInputOutputHandler(ctx, orderRepository, haClient)
    TradingHttpServer(ctx.system.classicSystem, orderRepository)

    Behaviors.empty[ExternalCommand]
  }

  typed.ActorSystem(behavior, "TradingSystem", config)
}
