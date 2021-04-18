package com.knoldus.trading.handler

import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.scaladsl.Behaviors
import com.knoldus.common.command.{BookedOrder, CreateNewOrder, ExternalCommand}
import com.knoldus.trading.engine.LookupActor.LookForActor
import com.knoldus.trading.engine.{LookupActor, OrderActor}
import com.knoldus.trading.model.OrderModel.Order
import com.knoldus.trading.state.OrderStatus

object RestInputHandler {

  private var getUUID: Int = 0

  def apply(): Behavior[ExternalCommand] = Behaviors.setup { ctx =>
    Behaviors.receiveMessage[ExternalCommand] {
      case msg: CreateNewOrder =>
        val orderId = (getUUID + 1).toString
        val order = Order(orderId, msg.order.side, msg.order.price, msg.order.quantity, msg.order.productCode,
          msg.order.productType, System.currentTimeMillis(), OrderStatus.New)

        val orderActor = ctx.spawn(OrderActor(orderId, order), orderId)
        ctx.system.receptionist ! Receptionist.Register(OrderActor.getServiceKey(orderId), orderActor)

        orderActor ! msg
        getUUID = getUUID + 1
        Behaviors.same
      case _ => Behaviors.same
    }
  }

}