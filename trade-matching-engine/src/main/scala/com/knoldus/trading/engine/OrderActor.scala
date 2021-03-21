package com.knoldus.trading.engine

import akka.Done
import akka.actor.typed.receptionist.ServiceKey
import akka.actor.typed.scaladsl.{ActorContext, Behaviors, LoggerOps}
import akka.actor.typed.{Behavior, PostStop}
import akka.persistence.typed.scaladsl.{Effect, EffectBuilder, EventSourcedBehavior}
import akka.persistence.typed.{PersistenceId, RecoveryCompleted}
import com.knoldus.common.command.Command
import com.knoldus.common.event.Event
import com.knoldus.trading.model.OrderModel.Order
import com.knoldus.trading.state.OrderState
import org.slf4j.Logger

import scala.concurrent.Future

object OrderActor {

  def getServiceKey(id: String): ServiceKey[Command] = ServiceKey[Command](id)

  def apply(persistenceId: String, order: Order): Behavior[Command] = {
    Behaviors.setup[Command] { ctx =>
      new OrderActor(persistenceId, ctx).behavior(order)
    }
  }

  class OrderActor(persistenceId: String, ctx: ActorContext[Command]) {
    val logger: Logger = ctx.log
    logger.info("Actor with id {} started", persistenceId)

    def behavior(order: Order): EventSourcedBehavior[Command, Event, OrderState] = EventSourcedBehavior.apply[Command, Event, OrderState](
      PersistenceId.ofUniqueId(persistenceId),
      OrderStateMachine.newOrder(order),
      commandHandler,
      eventHandler
    ).receiveSignal {
      case (state, _@RecoveryCompleted) =>
        logger.info2("{} Recovery completed: {}", persistenceId, state)
      case (_, PostStop) => logger.info("{} Actor Terminated", persistenceId)
    }

    val commandHandler: (OrderState, Command) => Effect[Event, OrderState] = (current: OrderState, cmd: Command) => {
      logger.infoN("{} -- Command : {} => Current State :{}", persistenceId, cmd, current)

      def invalidCommandHandler(): EffectBuilder[Event, OrderState] = Effect.none[Event, OrderState].thenRun(state => exceptionHandler(state).apply(cmd))

      if (current.handleCommand.isDefinedAt(cmd)) {
        val (event, extEvt) = current.handleCommand(cmd)
        Effect.persist(event).thenRun { _ => publish(extEvt, event) }
      } else {
        invalidCommandHandler()
      }
    }

    val eventHandler: (OrderState, Event) => OrderState = (current: OrderState, evt: Event) => {
      current.onEvent.apply(evt)
    }

    private def exceptionHandler(state: OrderState): PartialFunction[Command, OrderState] = {
      case cmd: Command =>
        logger.error(s"Invalid command ${cmd}")
        state
    }

    def publish(events: Event*): Future[Done] = {
      events.foreach(evt => ctx.system.classicSystem.eventStream.publish(evt))
      Future.successful(Done)
    }
  }


}