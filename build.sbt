name := "json-test"

organization := "me.imykhailov"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.scalaj" % "scalaj-time_2.10.2" % "0.7",
  "com.typesafe.play" %% "play-json" % "2.3.1",
  "com.typesafe" % "config" % "1.2.0",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "org.json4s" %% "json4s" % "3.2.11",
  "org.json4s" %% "json4s-native" % "3.2.11",
  "org.json4s" %% "json4s-jackson" % "3.2.11",
  "org.json4s" %% "json4s-tests" % "3.2.11"
)

//Tests
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.0" % "test"
//  "com.typesafe.akka" %% "akka-testkit" % "2.2.0" % "test"
)


scalacOptions ++= Seq(
    "-deprecation"
  , "-feature"
  , "-unchecked"
  , "-Xlint"
  , "-Yno-adapted-args"
  , "-Ywarn-all"
  , "-Ywarn-dead-code"
  , "-language:postfixOps"
  , "-language:implicitConversions"
)

seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

net.virtualvoid.sbt.graph.Plugin.graphSettings
