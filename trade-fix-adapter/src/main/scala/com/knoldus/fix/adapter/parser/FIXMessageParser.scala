package com.knoldus.fix.adapter.parser

import com.knoldus.fix.adapter.models.NewOrderSingle
import org.slf4j.{Logger, LoggerFactory}
import quickfix.MessageUtils.getMessageType
import quickfix.field.{MsgType, _}
import quickfix.fix44.component.Instrument
import quickfix._

class FIXMessageParser {

  import FIXMessageParser._

  def logger: Logger = LoggerFactory.getLogger("FIXMessageProcessor")

  @throws[InvalidMessage]
  def parse(messageFactory: MessageFactory, dataDictionary: DataDictionary, messageString: String): Message = {

    val index = messageString.indexOf(FieldSeparator)
    if (index < 0) throw new InvalidMessage("Message does not contain any field separator")
    val beginString = messageString.substring(2, index)
    val messageType = getMessageType(messageString)
    val message = messageFactory.create(beginString, messageType)
    message.fromString(messageString, dataDictionary, dataDictionary != null)
    message
  }

  def parseOrderMessage(message: Message): Boolean = {
    if (isNewOrderSingle(message, NewOrderSingleMsgType)) {
      val newOrderSingle: NewOrderSingle = message.asInstanceOf[NewOrderSingle]
      isValidNewOrderSingle(newOrderSingle)
    } else {
      throw new Exception("Not a single order type")
    }
  }

  private def isValidNewOrderSingle(newOrderSingle: NewOrderSingle) = {
    if (isValidClOrdID(newOrderSingle)) {
      throw new IncorrectTagValue(ClOrdID.FIELD, s"${newOrderSingle.clOrdID.getValue}")
    }

    if (isValidOrdType(newOrderSingle)) {
      throw new IncorrectTagValue(OrdType.FIELD, s"${newOrderSingle.orderType.getValue}")
    }

    if (isValidProduct(newOrderSingle.instrument)) {
      throw new IncorrectTagValue(Product.FIELD, s"${newOrderSingle.instrument}")
    }

    if (isValidStrikePrice(newOrderSingle.instrument)) {
      throw new IncorrectTagValue(StrikePrice.FIELD, s"${newOrderSingle.instrument}")
    }

    if (isValidOrderQty(newOrderSingle)) {
      throw new IncorrectTagValue(OrderQty.FIELD, s"${newOrderSingle.orderQtyData}")
    }

    if (isValidSide(newOrderSingle)) {
      throw new IncorrectTagValue(Side.FIELD, s"${newOrderSingle.side.getValue}")
    }

    logger.info("The order message has been parsed successfully.")
    true
  }

  private def isNewOrderSingle(message: Message, newOrderSingleMsgType: String): Boolean = {
    message.getHeader.getString(MsgType.FIELD).equals(newOrderSingleMsgType)
  }

  private def isValidClOrdID(newOrderSingle: NewOrderSingle): Boolean = newOrderSingle.clOrdID.getValue.nonEmpty

  private def isValidOrdType(newOrderSingle: NewOrderSingle): Boolean = ValidOrderType.contains(newOrderSingle.orderType.getValue)

  private def isValidProduct(orderInstrument: Instrument): Boolean = orderInstrument.isSetProduct && ValidProduct.contains(orderInstrument.getProduct.getValue)

  private def isValidStrikePrice(orderInstrument: Instrument): Boolean = orderInstrument.isSetStrikePrice && orderInstrument.getStrikePrice.getValue > 0

  private def isValidOrderQty(newOrderSingle: NewOrderSingle): Boolean =
    newOrderSingle.orderQtyData.isSetOrderQty && newOrderSingle.orderQtyData.getOrderQty.getValue > 0

  private def isValidSide(newOrderSingle: NewOrderSingle): Boolean = ValidSide.contains(newOrderSingle.side.getValue)
}

object FIXMessageParser {
  val FieldSeparator = " "
  val NewOrderSingleMsgType = "D"
  val ValidOrderType = List(OrdType.MARKET, OrdType.LIMIT, OrdType.COUNTER_ORDER_SELECTION)
  val ValidSide = List(Side.BUY, Side.SELL)
  val ValidProduct = List(Product.COMMODITY, Product.CORPORATE) // TODO: Check for valid products
}
