package com.knoldus.trading.app

import akka.actor.typed
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import com.knoldus.common.command.ExternalCommand
import com.knoldus.trading.view.TradingHttpServer
import com.typesafe.config.{Config, ConfigFactory}

object TradingApp extends App {
  val config: Config = ConfigFactory.load()

  val behavior = Behaviors.setup[ExternalCommand] { ctx: ActorContext[ExternalCommand] =>
    BindingInputOutputHandler(ctx)
    TradingHttpServer(ctx.system.classicSystem)

    Behaviors.empty[ExternalCommand]
  }

  typed.ActorSystem(behavior, "TradingSystem", config)
}
