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
      akka_http_spray_json
    )
  )

lazy val tradeFixAdapter = (project in file("trade-fix-adapter")).
  settings(commonSettings: _*).
  settings(
    name := "trade-fix-adapter",
    libraryDependencies ++= Seq(
      quickFixj,
      akka_persistence_typed,
      akka_stream,
      akka_http_spray_json,
      h2,
      lift,
      logback,
      json4s,
      jansi,
      jodaTime,
      googleGuava
    )
  ).dependsOn(common)

lazy val tradeViewAdapter = (project in file("trade-view-adapter")).
  settings(commonSettings: _*).
  settings(
    name := "trade-view-adapter",
    libraryDependencies ++= Seq(
      megard_http,
      akka_persistence_typed,
      akka_stream,
      akka_http_spray_json,
      lift,
      logback,
      json4s,
      jansi,
      jodaTime,
      googleGuava
    )
  ).dependsOn(common)

lazy val tradeBookingAdapter = (project in file("trade-booking-adapter")).
  settings(commonSettings: _*).
  settings(
    name := "trade-booking-adapter",
    libraryDependencies ++= Seq(
      akka_persistence_typed,
      akka_stream,
      akka_http_spray_json,
      h2,
      lift,
      logback,
      json4s,
      jansi,
      jodaTime,
      googleGuava
    )
  ).dependsOn(common)

lazy val tradeMatchingEngine = (project in file("trade-matching-engine")).
  settings(commonSettings: _*).
  settings(
    name := "trade-matching-engine",
    libraryDependencies ++= Seq(
      akka_persistence_typed,
      akka_stream,
      akka_http_spray_json,
      h2,
      lift,
      logback,
      json4s,
      jansi,
      jodaTime,
      googleGuava
    )
  ).dependsOn(common)
lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "Hexagonal-Architecture"
  ).dependsOn(tradeMatchingEngine, tradeFixAdapter, tradeViewAdapter, tradeBookingAdapter)
  .aggregate(tradeMatchingEngine, tradeFixAdapter, tradeViewAdapter, tradeBookingAdapter)
