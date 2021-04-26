package com.knoldus.common.persistence.profiles

import com.knoldus.common.persistence.DBComponent
import com.knoldus.common.persistence.model.PersistenceOrder
import slick.lifted.ProvenShape

trait OrderProfile {
  this: DBComponent =>

  import dbConfig.profile.api._

  val orderProfileQuery: TableQuery[OrderProfileTable] = TableQuery[OrderProfileTable]

  class OrderProfileTable(tag: Tag) extends Table[PersistenceOrder](tag, "orders") {
    def * : ProvenShape[PersistenceOrder] = (
      orderId,
      side,
      price,
      quantity,
      productCode,
      productType,
      createdTimeStamp,
      status,
      source
      ).mapTo[PersistenceOrder]

    def orderId: Rep[String] = column[String]("order_id", O.PrimaryKey)

    def side: Rep[String] = column[String]("side")

    def price: Rep[Double] = column[Double]("price")

    def quantity: Rep[Int] = column[Int]("quantity")

    def productCode: Rep[Int] = column[Int]("product_code")

    def productType: Rep[String] = column[String]("product_type")

    def createdTimeStamp: Rep[Long] = column[Long]("created_timestamp")

    def status: Rep[String] = column[String]("status")

    def source: Rep[String] = column[String]("source")
  }

}
