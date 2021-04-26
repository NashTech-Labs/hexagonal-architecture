package com.knoldus.trading.handler

import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.scaladsl.Behaviors
import com.knoldus.common.command.{ExternalCommand, FixCreateNewOrder}
import com.knoldus.trading.engine.OrderActor
import com.knoldus.trading.model.OrderModel.Order
import com.knoldus.trading.state.OrderStatus

object FixInputHandler {
  private var getUUID: Int = 0

  def apply(): Behavior[ExternalCommand] = Behaviors.setup { ctx =>
    Behaviors.receiveMessage[ExternalCommand] {
      case msg: FixCreateNewOrder =>
        val orderId = (getUUID + 1).toString
        val order = Order(orderId, msg.order.side, msg.order.price, msg.order.quantity, msg.order.productCode,
          msg.order.productType, System.currentTimeMillis(), OrderStatus.New, msg.order.source.getOrElse(""))

        val orderActor = ctx.spawn(OrderActor(orderId, order), orderId)
        ctx.system.receptionist ! Receptionist.Register(OrderActor.getServiceKey(orderId), orderActor)

        orderActor ! msg
        getUUID = getUUID + 1
        Behaviors.same
      case _ => Behaviors.same
    }
  }
}