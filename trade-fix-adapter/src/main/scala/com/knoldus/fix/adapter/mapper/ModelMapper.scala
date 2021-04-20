package com.knoldus.fix.adapter.mapper

import com.knoldus.common.command.CreateNewOrder
import com.knoldus.common.model.OrderRequest.Order
import quickfix.field.{AvgPx, CumQty, ExecID, ExecType, LeavesQty, OrdStatus, OrderID}
import quickfix.fix44.{ExecutionReport, NewOrderSingle}

import java.util.UUID

object ModelMapper {

  def mapNewOrderSingleToCreateNewOrder(newOrderSingle: NewOrderSingle): CreateNewOrder = {
    CreateNewOrder(
      Order(newOrderSingle.getSide.getValue.toString,
        newOrderSingle.getPrice.getValue,
        newOrderSingle.getOrderQty.getValue.toInt,
        newOrderSingle.getProduct.getValue,
        newOrderSingle.getSecurityType.getValue
      )
    )
  }

  def createExecutionReport(newSingleOrder: NewOrderSingle): ExecutionReport =
    new ExecutionReport(
      new OrderID(newSingleOrder.getClOrdID.getValue),
      new ExecID(UUID.randomUUID().toString),
      new ExecType(ExecType.NEW),
      new OrdStatus(OrdStatus.ACCEPTED_FOR_BIDDING),
      newSingleOrder.getSide,
      new LeavesQty(newSingleOrder.getOrderQty.getValue),
      new CumQty(0),
      new AvgPx(0)
    )

}
