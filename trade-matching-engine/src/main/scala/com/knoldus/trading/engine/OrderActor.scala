package com.knoldus.trading.engine

import akka.Done
import akka.actor.typed.scaladsl.{ActorContext, Behaviors, LoggerOps}
import akka.actor.typed.{Behavior, PostStop}
import akka.persistence.typed.scaladsl.{Effect, EffectBuilder, EventSourcedBehavior}
import akka.persistence.typed.{PersistenceId, RecoveryCompleted}
import com.knoldus.common.command.{Command, CreateNewOrder}
import com.knoldus.common.event.Event
import com.knoldus.trading.model.OrderModel.Order
import com.knoldus.trading.state.OrderState
import org.slf4j.Logger

import scala.concurrent.{Future, Promise}

object OrderActor {
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

      cmd match {
        case _: CreateNewOrder =>
          if (current.handleCommand.isDefinedAt(cmd)) {
            val event = current.handleCommand(cmd)
            Effect.persist(event).thenRun { state: OrderState =>
              publish(event)
              state
            }
          } else {
            invalidCommandHandler()
          }
        case _ => invalidCommandHandler()
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

    def publish(event: Event): Future[Done] = {
      ctx.system.classicSystem.eventStream.publish(event)
      Promise.successful[Done](Done).future
    }
  }


}