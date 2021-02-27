package com.knoldus.trading.engine

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.knoldus.trading.command.OrderMatched
import com.knoldus.trading.engine.LookupActor.LookForActor
import com.knoldus.trading.event.{OrderCreated, OrderInternalEvent}
import com.knoldus.trading.model.OrderModel.Order

import scala.collection.mutable.ListBuffer

object MatchingActor {

  private case class PerformOrderMatching(matchOrder: Order) extends OrderInternalEvent

  private var activeOrders: List[Order] = List.empty

  def apply(): Behavior[OrderInternalEvent] = Behaviors.setup { ctx =>
    Behaviors.receiveMessage {
      case msg: OrderCreated =>
          activeOrders = activeOrders ++ List(msg.order)
        println(s"matching order count ${activeOrders.size}")
        ctx.self ! PerformOrderMatching(msg.order)
        Behaviors.same
      case PerformOrderMatching(order) =>
        getMatchableOrder(order).foreach { case (order1, order2) =>
          ctx.spawnAnonymous(LookupActor.apply()) ! LookForActor(order1.orderId, OrderActor.getServiceKey(order1.orderId), OrderMatched(order2.orderId))
          ctx.spawnAnonymous(LookupActor.apply()) ! LookForActor(order2.orderId, OrderActor.getServiceKey(order2.orderId), OrderMatched(order1.orderId))

          activeOrders = activeOrders.filterNot(o => o.orderId == order1.orderId || o.orderId == order2.orderId)
        }

        Behaviors.same
      case _ => Behaviors.same
    }
  }

  private def getMatchableOrder(order: Order): Option[(Order, Order)] = {
    val eligibleOrderForMatching = activeOrders.filter { o =>
      o.orderId != order.orderId &&
        o.productType == order.productType &&
        o.productCode == order.productCode &&
        o.side != order.side
    }

    eligibleOrderForMatching.find(el => isMatchableOrder(order, el)) match {
      case None    => None
      case Some(e) => Some((e, order))
    }
  }

  private def isMatchableOrder(orderToMatch: Order, eligibleOrderForMatching: Order): Boolean = {
    orderToMatch.quantity == eligibleOrderForMatching.quantity && orderToMatch.price == eligibleOrderForMatching.price
  }

}
