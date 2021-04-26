package com.knoldus.fix.adapter.parser

import org.slf4j.{Logger, LoggerFactory}
import quickfix._
import quickfix.field.{MsgType, _}
import quickfix.fix44.NewOrderSingle

import scala.compat.java8.OptionConverters._

object FIXMessageParser {

  val logger: Logger = LoggerFactory.getLogger("FIXMessageProcessor")
  val FieldSeparator = "\001"
  val NewOrderSingleMsgType = "D"
  val ValidOrderType = List(OrdType.MARKET, OrdType.LIMIT, OrdType.COUNTER_ORDER_SELECTION)
  val ValidSide = List(Side.BUY, Side.SELL)
  val ValidProduct = List(Product.COMMODITY, Product.CORPORATE) // TODO: Check for valid products

  @throws[InvalidMessage]
  def parse(messageFactory: MessageFactory, dataDictionary: DataDictionary, messageString: String, session: Session): NewOrderSingle = {

    try {
      val a: Message = MessageUtils.parse(messageFactory, dataDictionary, messageString)
      val newOrderSingle = parseOrderMessage(a)
      logger.info(s"New Order Single message : $newOrderSingle")
      newOrderSingle
    } catch {
      case e: Exception => logger.error(s"Error: $e")
        //TODO: Handle exception
        throw new Exception("Not a valid order")
    }
  }

  def parseOrderMessage(message: Message): NewOrderSingle = {
    if (isNewOrderSingle(message, NewOrderSingleMsgType)) {
      getNewOrderSingle(message)
    } else {
      throw new Exception("Not a single order type")
    }
  }


  private def getNewOrderSingle(message: Message): NewOrderSingle = {
    val newOrderSingle: fix44.NewOrderSingle = new NewOrderSingle()

    newOrderSingle.set(new ClOrdID(getClOrdID(message)))
    newOrderSingle.set(new Side(getSide(message)))
    newOrderSingle.set(new Price(getPrice(message)))
    newOrderSingle.set(new OrderQty(getOrderQty(message)))
    newOrderSingle.set(new Product(getProduct(message)))
    newOrderSingle.set(new SecurityType(getSecurityType(message)))

    logger.info(s"The order message has been parsed successfully and converted to newOrderSingle : $newOrderSingle")
    newOrderSingle
  }

  private def isNewOrderSingle(message: Message, newOrderSingleMsgType: String): Boolean = {
    message.getHeader.getString(MsgType.FIELD).equals(newOrderSingleMsgType)
  }

  private def getClOrdID(message: Message): String = {
    message.getOptionalString(ClOrdID.FIELD).asScala.getOrElse(throw new IncorrectTagValue(ClOrdID.FIELD))
  }

  private def getSide(message: Message): Char = {
    val side = message.getChar(Side.FIELD)
    if (ValidSide.contains(side)) side else throw new IncorrectTagValue(Side.FIELD)
  }

  private def getPrice(message: Message): Double = {
    val price = message.getDouble(Price.FIELD)
    if (price > 0) price else throw new IncorrectTagValue(Price.FIELD)
  }

  private def getOrderQty(message: Message): Double = {
    val orderQty = message.getDouble(OrderQty.FIELD)
    if (orderQty > 0) orderQty else throw new IncorrectTagValue(OrderQty.FIELD)
  }

  private def getProduct(message: Message): Int = {
    val product = message.getInt(Product.FIELD)
    if (product > 0) product else throw new IncorrectTagValue(SecurityType.FIELD)
  }

  private def getSecurityType(message: Message): String =
    message.getOptionalString(SecurityType.FIELD).asScala.getOrElse(throw new IncorrectTagValue(SecurityType.FIELD))

}
