package com.knoldus.booking.adapter.handler

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.knoldus.booking.adapter.model.TradeBookingModel.TradeReport
import com.knoldus.booking.adapter.model.TradeStatus
import com.knoldus.common.command.BookedOrder
import com.knoldus.common.event.{BookingOrderRequest, ExternalEvent}
import com.knoldus.common.persistence.CassandraOperation
import spray.json._

object TradeBookingOutputHandler {

  import com.knoldus.booking.adapter.service.JsonHelper._

  private var getUUID: Int = 0

  def apply(): Behavior[ExternalEvent] = Behaviors.setup { ctx =>
    Behaviors.receiveMessage {
      case evt: BookingOrderRequest =>
        val tradeId = (getUUID + 1).toString
        ctx.log.info(s" Booking req :: ${evt}")
        CassandraOperation.insertTradeReport(getTradeReport(tradeId, evt).toJson.toString())
        evt.orderIds.foreach { orderId =>
          ctx.system.classicSystem.eventStream.publish(BookedOrder(orderId))
        }
        getUUID = getUUID + 1
        Behaviors.same
      case _ => Behaviors.same
    }
  }

  private def getTradeReport(tradeId: String, bookingReq: BookingOrderRequest): TradeReport = {
    TradeReport(tradeId, bookingReq.price, bookingReq.volume,
      bookingReq.productCode, bookingReq.productType, System.currentTimeMillis(),
      bookingReq.orderIds, TradeStatus.Booked)
  }
}
