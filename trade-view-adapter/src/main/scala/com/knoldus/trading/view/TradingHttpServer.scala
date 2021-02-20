package com.knoldus.trading.view

import akka.actor
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.Materializer
import com.knoldus.trading.view.service.OrderApi
import com.typesafe.config.Config

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object TradingHttpServer {

  def apply(actorSystem: ActorSystem[Nothing],
            config: Config) = {
    implicit val classicSystem: actor.ActorSystem = actorSystem.classicSystem
    implicit val materializer: Materializer = Materializer(classicSystem)
    implicit val executionCtx: ExecutionContextExecutor = classicSystem.dispatcher

    val (hostname, port) = (config.getString("http.hostname"), config.getString("http.port").toInt)
    val routes = new OrderApi().routes
    Http().bindAndHandle(routes, hostname, port).onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        actorSystem.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        actorSystem.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        actorSystem.terminate()
    }(actorSystem.executionContext)
  }
}
