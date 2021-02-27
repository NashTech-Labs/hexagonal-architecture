import sbt._

object Dependencies {

  val akkaActorVersion = "2.6.5"
  val akkaHttpVersion = "10.1.11"
  val akkaStreamKafkaVersion = "2.0.3"
  val cassandraVersion = "3.1.0"

  lazy val akka_persistence_typed       = "com.typesafe.akka"              %% "akka-persistence-typed"         % akkaActorVersion
  lazy val akka_persistence_query       = "com.typesafe.akka"              %% "akka-persistence-query"         % akkaActorVersion
  lazy val akka_stream                  = "com.typesafe.akka"              %% "akka-stream"                    % akkaActorVersion
  lazy val akka_typed                   = "com.typesafe.akka"              %% "akka-actor-typed"               % akkaActorVersion
  lazy val akka_http_spray_json         = "com.typesafe.akka"              %% "akka-http-spray-json"           % akkaHttpVersion
  lazy val akka_persistence_jdbc        = "com.lightbend.akka"             %% "akka-persistence-jdbc"          % "4.0.0"
  lazy val akka_slick                   = "com.lightbend.akka"             %% "akka-stream-alpakka-slick"      % "2.0.0"
  lazy val akka_serialization_jackson   = "com.typesafe.akka"              %% "akka-serialization-jackson"     % akkaActorVersion
  lazy val cassandra                    = "com.datastax.cassandra"         % "cassandra-driver-core"           % cassandraVersion

  lazy val lable_db                     = "org.fusesource.leveldbjni"      % "leveldbjni-all"                  % "1.8"
  lazy val level_db                     = "org.iq80.leveldb"               % "leveldb"                         % "0.7" exclude("com.google.guava", "guava")
  lazy val logback                      = "ch.qos.logback"                 % "logback-classic"                 % "1.2.3"
  lazy val megard_http                  = "ch.megard"                      %% "akka-http-cors"                 % "0.4.1"
  lazy val scala_logging                = "com.typesafe.scala-logging"     %% "scala-logging"                  % "3.9.0"
  lazy val h2                           = "com.h2database"                 % "h2"                              % "1.4.197"

  lazy val lift                         =  "net.liftweb"                   %% "lift-json"                      % "3.0.1"
  lazy val json4s                       =  "org.json4s"                    %% "json4s-native"                  % "3.4.2"
  lazy val jansi                        =  "org.fusesource.jansi"          % "jansi"                           % "1.12"
  lazy val jodaTime                     =  "joda-time"                     % "joda-time"                       % "2.10.6"
  lazy val googleGuava                  =  "com.google.guava"              % "guava"                           % "19.0"
  lazy val quickFixj                    =  "org.quickfixj"                 % "quickfixj-messages-fix44"        % "2.2.0"
}
