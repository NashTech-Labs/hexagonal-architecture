package com.knoldus.common.persistence.repository

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

class OrderRepositoryImpl(dbConf: DatabaseConfig[JdbcProfile]) extends ProfilesDBComponent(dbConf)
  with OrderRepository
