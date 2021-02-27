import Dependencies._

name := """Hexagonal-Architecture"""

lazy val commonSettings = Seq(
  organization := "com.knoldus",
  version := "0.1.0",
  scalaVersion := "2.12.12"
)

lazy val common = (project in file("common"))
  .settings(commonSettings,
    name := "common",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.4.1",
      logback,
      cassandra,
      h2,
      akka_slick,
      akka_http_spray_json
    )
  )

lazy val tradeFixAdapter = (project in file("trade-fix-adapter")).
  settings(commonSettings: _*).
  settings(
    name := "trade-fix-adapter",
    libraryDependencies ++= Seq(
      logback,
      quickFixj,
      akka_persistence_typed,
      akka_stream,
      akka_http_spray_json,
      h2
    )
  ).dependsOn(common)

lazy val tradeViewAdapter = (project in file("trade-view-adapter")).
  settings(commonSettings: _*).
  settings(
    name := "trade-view-adapter",
    libraryDependencies ++= Seq(
      logback,
      megard_http,
      akka_persistence_typed,
      akka_stream,
      akka_http_spray_json
    )
  ).dependsOn(common)

lazy val tradeBookingAdapter = (project in file("trade-booking-adapter")).
  settings(commonSettings: _*).
  settings(
    name := "trade-booking-adapter",
    libraryDependencies ++= Seq(
      logback,
      akka_persistence_typed,
      akka_stream,
      akka_http_spray_json,
      h2
    )
  ).dependsOn(common)

lazy val tradeMatchingEngine = (project in file("trade-matching-engine")).
  settings(commonSettings: _*).
  settings(
    name := "trade-matching-engine",
    libraryDependencies ++= Seq(
      logback,
      akka_persistence_typed,
      akka_stream,
      akka_http_spray_json,
      akka_slick,
      akka_persistence_jdbc,
      h2
    )
  ).dependsOn(common)
lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "Hexagonal-Architecture"
  ).dependsOn(tradeMatchingEngine, tradeFixAdapter, tradeViewAdapter, tradeBookingAdapter)
  .aggregate(tradeMatchingEngine, tradeFixAdapter, tradeViewAdapter, tradeBookingAdapter)
