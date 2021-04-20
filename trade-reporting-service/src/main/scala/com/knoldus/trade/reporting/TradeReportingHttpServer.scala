package com.knoldus.trade.reporting

import akka.actor
import akka.actor.typed
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.http.scaladsl.Http
import akka.stream.Materializer
import com.crankuptheamps.client.HAClient
import com.knoldus.common.amps.AmpsClient
import com.knoldus.common.command.Command
import com.knoldus.trade.reporting.service.{TradeReportService, TradeReportingApi}
import com.knoldus.trade.reporting.util.ConfigUtil.{CLIENT_NAME, SERVER_IPS}
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object TradeReportingHttpServer extends App {
  val config: Config = ConfigFactory.load()
  val behavior = Behaviors.setup[Command] { ctx: ActorContext[Command] =>
    implicit val classicSystem: actor.ActorSystem = ctx.system.classicSystem
    implicit val materializer: Materializer = Materializer(classicSystem)
    implicit val executionCtx: ExecutionContextExecutor = classicSystem.dispatcher
    val (hostname, port) = (config.getString("http.host"), config.getString("http.port").toInt)
    val hAClient: HAClient = AmpsClient.getConnection(CLIENT_NAME, SERVER_IPS)
    val routes = new TradeReportingApi().routes
    Http().bindAndHandle(routes, hostname, port).onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        ctx.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
        TradeReportService(hAClient)
      case Failure(ex) =>
        ctx.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        classicSystem.terminate()
    }(executionCtx)
    Behaviors.same
  }

  typed.ActorSystem(behavior, "TradeReportingSystem", config)
}
