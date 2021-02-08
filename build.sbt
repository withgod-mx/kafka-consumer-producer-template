name := "kafka-consumer-producer-template"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.8",
  "com.typesafe.akka" %% "akka-stream" % "2.6.8",
  "com.typesafe.akka" %% "akka-stream-kafka" % "2.0.4",
  "com.typesafe.akka" %% "akka-http" % "10.2.0",

  "org.apache.kafka" %% "kafka" % "2.6.0",
  "org.apache.kafka" % "kafka-clients" % "2.6.0",


  /*
    // for JSON in Scala
    "io.spray" %% "spray-json" % "1.3.5",

    "io.circe" %% "circe-core" % "0.13.0",
    "io.circe" %% "circe-generic" % "0.13.0",
    "io.circe" %% "circe-jawn" % "0.13.0",
    "io.circe" %% "circe-parser" % "0.14.0-M1",*/


  // Logging
  "com.typesafe.akka" %% "akka-slf4j" % "2.6.8",
  "ch.qos.logback" % "logback-classic" % "1.2.3"

)
