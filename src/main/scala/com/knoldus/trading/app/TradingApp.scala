package com.knoldus.trading.app

import akka.actor.typed
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import com.knoldus.common.command.ExternalCommand
import com.knoldus.common.persistence.repository.{OrderRepository, OrderRepositoryImpl}
import com.knoldus.trading.view.TradingHttpServer
import com.typesafe.config.{Config, ConfigFactory}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

object TradingApp extends App {
  val config: Config = ConfigFactory.load()
  val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("slick")
  val orderRepository: OrderRepository = new OrderRepositoryImpl(dbConfig)

  val behavior = Behaviors.setup[ExternalCommand] { ctx: ActorContext[ExternalCommand] =>
    BindingInputOutputHandler(ctx, orderRepository)
    TradingHttpServer(ctx.system.classicSystem, orderRepository)

    Behaviors.empty[ExternalCommand]
  }

  typed.ActorSystem(behavior, "TradingSystem", config)
}
