package com.knoldus.trading.engine

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import com.knoldus.common.command.Command

object LookupActor {

  trait LookUpActorCommand

  case class LookForActor(id: String, serviceKey: ServiceKey[Command], action: Command) extends LookUpActorCommand

  private case class LookupResponse(lookupActorId: String, listing: Receptionist.Listing, action: Command) extends LookUpActorCommand

  def apply(): Behavior[LookUpActorCommand] = Behaviors.setup { ctx =>
    Behaviors.receiveMessage {
      case cmd: LookForActor =>
        val listingResponseAdapter: ActorRef[Receptionist.Listing] = ctx.messageAdapter(l => LookupResponse(cmd.id, l, cmd.action))
        ctx.system.receptionist ! Receptionist.Find(cmd.serviceKey, listingResponseAdapter)
        Behaviors.same
      case cmd: LookupResponse =>
        cmd.listing.serviceInstances(OrderActor.getServiceKey(cmd.lookupActorId)).foreach(ref => ref ! cmd.action)
      Behaviors.stopped
    }
  }

}
