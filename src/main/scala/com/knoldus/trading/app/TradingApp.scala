package com.knoldus.trading.app

import akka.actor.typed
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import com.knoldus.common.event.Event
import com.typesafe.config.{Config, ConfigFactory}
object TradingApp extends App {
  val config: Config = ConfigFactory.load().getConfig(s"trading-system")

  val behavior = Behaviors.setup[Event] { ctx: ActorContext[Event] =>


    Behaviors.empty[Event]
  }

   typed.ActorSystem(behavior, "TradingSystem", config)
  /*implicit val actorSystem = ActorSystem( "FileToElasticSearch")

  implicit val executionContext: ExecutionContext = actorSystem.dispatcher
  val jazzListener = actorSystem.actorOf(Props[FixInputHandler]())
  val musicListener = actorSystem.actorOf(Props[FixInputHandler]())
  actorSystem.eventStream.subscribe(jazzListener, classOf[Jazz])
  actorSystem.eventStream.subscribe(musicListener, classOf[AllKindsOfMusic])
  // only musicListener gets this message, since it listens to *all* kinds of music:
  actorSystem.eventStream.publish(Electronic("Parov Stelar"))
  // jazzListener and musicListener will be notified about Jazz:
  actorSystem.eventStream.publish(Jazz("Sonny Rollins"))*/

}