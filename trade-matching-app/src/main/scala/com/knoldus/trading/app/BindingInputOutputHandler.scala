package com.knoldus.trading.app

import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.adapter.TypedActorRefOps
import com.crankuptheamps.client.HAClient
import com.knoldus.trading.handler.TradingOutputHandler
//import com.knoldus.booking.adapter.handler.TradeBookingOutputHandler
import com.knoldus.common.command.ExternalCommand
import com.knoldus.common.event.ExternalEvent
import com.knoldus.common.persistence.repository.OrderRepository
import com.knoldus.trading.engine.MatchingActor
import com.knoldus.trading.event.OrderInternalEvent
import com.knoldus.trading.handler.RestInputHandler
import com.knoldus.trading.view.handler.ViewOutputHandler

object BindingInputOutputHandler {

  def apply(ctx: ActorContext[ExternalCommand], orderRepository: OrderRepository,
            haClient: HAClient): Boolean = {
    val restActorRef = ctx.spawnAnonymous(RestInputHandler.apply())
    val viewActorRef = ctx.spawnAnonymous(ViewOutputHandler.apply(orderRepository))
    val matchingActorRef = ctx.spawnAnonymous(MatchingActor.apply())
    val tradingOutputActorRef = ctx.spawnAnonymous(TradingOutputHandler.apply(haClient))
    ctx.system.classicSystem.eventStream.subscribe(restActorRef.toClassic, classOf[ExternalCommand])
    ctx.system.classicSystem.eventStream.subscribe(viewActorRef.toClassic, classOf[ExternalEvent])
    ctx.system.classicSystem.eventStream.subscribe(tradingOutputActorRef.toClassic, classOf[ExternalEvent])
    ctx.system.classicSystem.eventStream.subscribe(matchingActorRef.toClassic, classOf[OrderInternalEvent])
  }
}
