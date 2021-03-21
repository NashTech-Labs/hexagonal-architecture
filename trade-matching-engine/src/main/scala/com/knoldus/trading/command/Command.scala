package com.knoldus.trading.command

import com.knoldus.common.command.Command

trait InternalCommand extends Command

case class OrderMatched(matchedWithOrderId: String) extends InternalCommand
