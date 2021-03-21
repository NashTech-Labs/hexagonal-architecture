package com.knoldus.fix.adapter.models

import quickfix.field._
import quickfix.fix44.component.Instrument
import quickfix.fix44.component._

case class NewOrderSingle(clOrdID: ClOrdID,
                          orderType: OrdType,
                          securityType: SecurityType,
                          instrument: Instrument, // TODO - check what is comprises of
                          orderQtyData: OrderQtyData,
                          side: Side,
                          price: Price,
                          transactTime: TransactTime) {
}
