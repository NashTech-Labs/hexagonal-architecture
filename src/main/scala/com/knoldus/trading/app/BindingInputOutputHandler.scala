package com.knoldus.trading.app

import akka.actor.Props
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.adapter.{TypedActorContextOps, TypedActorRefOps}
import com.knoldus.common.command.{CreateNewOrder, ExternalCommand}
import com.knoldus.common.event.{Event, ExternalEvent}
import com.knoldus.common.model.OrderRequest.Order
import com.knoldus.trading.engine.MatchingActor
import com.knoldus.trading.event.OrderInternalEvent
import com.knoldus.trading.handler.{FixInputHandler, RestInputHandler}
import com.knoldus.trading.view.handler.ViewOutputHandler

object BindingInputOutputHandler {
  def apply (ctx: ActorContext[ExternalCommand]) = {
    val restActorRef = ctx.spawnAnonymous(RestInputHandler.apply())
    val viewActorRef = ctx.spawnAnonymous(ViewOutputHandler.apply())
    val matchingActorRef = ctx.spawnAnonymous(MatchingActor.apply())
    ctx.system.classicSystem.eventStream.subscribe(restActorRef.toClassic, classOf[ExternalCommand])
    ctx.system.classicSystem.eventStream.subscribe(viewActorRef.toClassic, classOf[ExternalEvent])
    ctx.system.classicSystem.eventStream.subscribe(matchingActorRef.toClassic, classOf[OrderInternalEvent])
 }
}
