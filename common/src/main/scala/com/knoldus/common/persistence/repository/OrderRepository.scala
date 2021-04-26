package com.knoldus.common.persistence.repository

import com.knoldus.common.model.OrderResponse.OrderCreatedResponse
import com.knoldus.common.persistence.DBComponent
import com.knoldus.common.persistence.model.PersistenceOrder
import com.knoldus.common.persistence.profiles.OrderProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait OrderRepository extends OrderProfile {
  this: DBComponent =>

  import dbConfig.profile.api._

  def getOrderById(orderId: String): Future[Option[OrderCreatedResponse]] = db.run {
    val query = orderProfileQuery.filter(_.orderId === orderId)
    query.result.map(_.headOption).map { optOrder =>
      optOrder.map { order =>
        OrderCreatedResponse(order.orderId, order.side, order.price, order.quantity, order.productCode, order.productType, order.timeStamp,
          order.status, order.source)
      }
    }
  }

  def getAllOrders: Future[Seq[OrderCreatedResponse]] = db.run {
    orderProfileQuery.result.map { optOrder =>
      optOrder.map { order =>
        OrderCreatedResponse(order.orderId, order.side, order.price, order.quantity, order.productCode, order.productType, order.timeStamp,
          order.status, order.source)
      }
    }
  }

  def insertOrder(persistenceOrder: PersistenceOrder): Future[Int] = db.run {
    orderProfileQuery.insertOrUpdate(persistenceOrder)
  }
}
