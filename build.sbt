import Dependencies._

name := """Hexagonal-Architecture"""

lazy val commonSettings = Seq(
  organization := "com.knoldus",
  version := "0.1.0",
  scalaVersion := "2.12.12"
)

lazy val fixAdapter = (project in file("fix-adapter")).
  settings(commonSettings: _*).
  settings(
    name := "fix-adapter",
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
  )

lazy val viewAdapter = (project in file("view-adapter")).
  settings(commonSettings: _*).
  settings(
    name := "view-adapter",
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
  )

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
  )

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
  )
lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "Hexagonal-Architecture"
  ).aggregate(tradeMatchingEngine, fixAdapter, viewAdapter, tradeBookingAdapter)
