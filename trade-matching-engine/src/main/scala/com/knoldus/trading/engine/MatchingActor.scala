package com.knoldus.trading.engine

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.knoldus.common.command.BookedOrder
import com.knoldus.common.event.BookingOrderRequest
import com.knoldus.trading.command.OrderMatched
import com.knoldus.trading.engine.LookupActor.LookForActor
import com.knoldus.trading.event.{OrderCreated, OrderInternalEvent}
import com.knoldus.trading.model.OrderModel.Order

object MatchingActor {

  private case class PerformOrderMatching(matchOrder: Order) extends OrderInternalEvent

  private case class PerformOrderBooking(matchedOrder1: Order, matchedOrder2: Order) extends OrderInternalEvent

  private var activeOrders: List[Order] = List.empty

  def apply(): Behavior[OrderInternalEvent] = Behaviors.setup { ctx =>
    Behaviors.receiveMessage {
      case msg: OrderCreated =>
        activeOrders = activeOrders ++ List(msg.order)
        ctx.log.info(s"matching order count ${activeOrders.size}")
        ctx.self ! PerformOrderMatching(msg.order)
        Behaviors.same
      case PerformOrderMatching(order) =>
        getMatchableOrder(order).foreach { case (order1, order2) =>
          ctx.spawnAnonymous(LookupActor.apply()) ! LookForActor(order1.orderId, OrderActor.getServiceKey(order1.orderId), OrderMatched(order2.orderId))
          ctx.spawnAnonymous(LookupActor.apply()) ! LookForActor(order2.orderId, OrderActor.getServiceKey(order2.orderId), OrderMatched(order1.orderId))
          ctx.self ! PerformOrderBooking(order1, order2)
          activeOrders = activeOrders.filterNot(o => o.orderId == order1.orderId || o.orderId == order2.orderId)
        }

        Behaviors.same
      case PerformOrderBooking(order1, order2) =>
        ctx.log.info(s"sending booking order ${order1}")
        ctx.system.classicSystem.eventStream.publish(getBookingOrderRequest(order1, order2))
        ctx.spawnAnonymous(LookupActor.apply()) ! LookForActor(order1.orderId, OrderActor.getServiceKey(order1.orderId), BookedOrder(order1.orderId))
        ctx.spawnAnonymous(LookupActor.apply()) ! LookForActor(order2.orderId, OrderActor.getServiceKey(order2.orderId), BookedOrder(order2.orderId))
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
      case None => None
      case Some(e) => Some((e, order))
    }
  }

  private def isMatchableOrder(orderToMatch: Order, eligibleOrderForMatching: Order): Boolean = {
    orderToMatch.quantity == eligibleOrderForMatching.quantity && orderToMatch.price == eligibleOrderForMatching.price
  }

  private def getBookingOrderRequest(order1: Order, order2: Order): BookingOrderRequest = {
    val orderIds = List(order1, order2).map(_.orderId)
    BookingOrderRequest(order1.price, order1.quantity,
      order1.productCode, order1.productType, orderIds)
  }
}
