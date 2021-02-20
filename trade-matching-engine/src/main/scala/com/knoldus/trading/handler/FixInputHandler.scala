package com.knoldus.trading.handler

import akka.actor.Actor
abstract class AllKindsOfMusic { def artist: String }
case class Jazz(artist: String) extends AllKindsOfMusic
case class Electronic(artist: String) extends AllKindsOfMusic
class FixInputHandler extends Actor {
  def receive = {
    case m: Jazz       => println(s"${self.path.name} is listening to: ${m.artist}")
    case m: Electronic => println(s"${self.path.name} is listening to: ${m.artist}")
  }
}