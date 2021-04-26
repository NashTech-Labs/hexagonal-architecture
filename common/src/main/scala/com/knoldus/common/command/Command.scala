package com.knoldus.common.command

import com.knoldus.common.model.OrderRequest.Order

trait Command

trait ExternalCommand extends Command

final case class CreateNewOrder(order: Order) extends ExternalCommand

final case class FixCreateNewOrder(order: Order) extends ExternalCommand

final case class BookedOrder(orderId: String) extends ExternalCommand