package com.knoldus.trading.view.handler

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.knoldus.common.event.{ExternalEvent, MatchedOrderResponse, NewOrderCreated}

object ViewOutputHandler {

  def apply(): Behavior[ExternalEvent] = Behaviors.receiveMessage {
    case evt :NewOrderCreated =>
      println(s"@@ view ${evt}")
      Behaviors.same
    case evt: MatchedOrderResponse =>
      println(s" @@@ order has been matched ${evt}")
      Behaviors.same
    case _ => Behaviors.ignore
  }
}
