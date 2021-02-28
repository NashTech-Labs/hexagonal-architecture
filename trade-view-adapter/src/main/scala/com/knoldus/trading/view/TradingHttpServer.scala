package com.knoldus.trading.view

import akka.actor
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.Materializer
import com.knoldus.common.persistence.repository.OrderRepository
import com.knoldus.trading.view.service.OrderApi
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object TradingHttpServer {

  def apply(ctx: ActorSystem, orderRepository: OrderRepository) = {
    implicit val classicSystem: actor.ActorSystem = ctx
    implicit val materializer: Materializer = Materializer(classicSystem)
    implicit val executionCtx: ExecutionContextExecutor = classicSystem.dispatcher
    val config: Config = ConfigFactory.load().getConfig(s"http")
    val (hostname, port) = (config.getString("host"), config.getString("port").toInt)

    val routes = new OrderApi(ctx, orderRepository).routes
    Http().bindAndHandle(routes, hostname, port).onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        ctx.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        ctx.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        ctx.terminate()
    }(executionCtx)
  }
}
