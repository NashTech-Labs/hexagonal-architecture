package com.knoldus.common.persistence.repository

import com.knoldus.common.persistence.DBComponent
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

class ProfilesDBComponent(dbConf: DatabaseConfig[JdbcProfile]) extends DBComponent {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConf
  val db: JdbcProfile#Backend#Database = dbConfig.db
}
