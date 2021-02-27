package com.knoldus.common.persistence

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait DBComponent {
  val dbConfig: DatabaseConfig[JdbcProfile]
  val db: JdbcProfile#Backend#Database
}
