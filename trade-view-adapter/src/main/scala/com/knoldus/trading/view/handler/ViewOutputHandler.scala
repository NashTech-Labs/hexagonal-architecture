package com.knoldus.trading.view.handler

import akka.actor.Actor
import com.knoldus.common.event.OrderCreated

class ViewOutputHandler extends Actor {
  override def receive: Receive = {
    case evt :OrderCreated => println(s"@@ view ${evt}")
  }
}
