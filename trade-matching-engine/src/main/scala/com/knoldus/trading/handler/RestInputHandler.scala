package com.knoldus.trading.handler

import java.util.UUID

import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.scaladsl.Behaviors
import com.knoldus.common.command.{CreateNewOrder, ExternalCommand}
import com.knoldus.trading.engine.OrderActor
import com.knoldus.trading.model.OrderModel.Order
import com.knoldus.trading.state.OrderStatus

object RestInputHandler {

//  private def getUUID: String = UUID.randomUUID().toString
  private var getUUID: Int = 0

  def apply(): Behavior[ExternalCommand] = Behaviors.setup { ctx =>
    Behaviors.receiveMessage[ExternalCommand] {
      case msg: CreateNewOrder =>
        val orderId = (getUUID + 1).toString
        getUUID = getUUID + 1
        val order = Order(orderId, msg.order.side, msg.order.price, msg.order.quantity, msg.order.productCode,
          msg.order.productType, System.currentTimeMillis(), OrderStatus.New)

        val orderActor = ctx.spawn(OrderActor(orderId, order), orderId)
        ctx.system.receptionist ! Receptionist.Register(OrderActor.getServiceKey(orderId), orderActor)

        orderActor ! msg
        Behaviors.same
      case _ => Behaviors.ignore
    }

  }

}