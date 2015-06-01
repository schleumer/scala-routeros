name := "Scala RouterOS API client"

version := "0.1"

scalaVersion := "2.11.6"

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
)

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4-SNAPSHOT",
  "org.specs2" %% "specs2-core" % "3.6" % "test"
)

fork in Test := true


Revolver.settings