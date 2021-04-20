package com.knoldus.fix.adapter.mapper

import com.knoldus.common.command.CreateNewOrder
import com.knoldus.common.model.OrderRequest.Order
import quickfix.fix44.NewOrderSingle

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
}
