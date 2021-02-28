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

  lazy val logback                      = "ch.qos.logback"                 % "logback-classic"                 % "1.2.3"
  lazy val h2                           = "com.h2database"                 % "h2"                              % "1.4.197"

  lazy val quickFixj                    =  "org.quickfixj"                 % "quickfixj-messages-fix44"        % "2.2.0"
}
