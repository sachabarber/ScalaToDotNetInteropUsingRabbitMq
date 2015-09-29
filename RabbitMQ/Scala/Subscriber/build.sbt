name := "Subscriber"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.rabbitmq" % "amqp-client" % "3.5.5",
  "com.typesafe.play" % "play-json_2.11" % "2.4.3"
)