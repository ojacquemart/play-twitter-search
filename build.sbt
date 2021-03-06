import de.johoop.jacoco4sbt._
import JacocoPlugin._

name := """play-twitter-search"""

version := "1.0-SNAPSHOT"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)

lazy val macros = project

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .dependsOn(macros)

scalaVersion := "2.11.1"

jacoco.settings

parallelExecution      in jacoco.Config := false

jacoco.outputDirectory in jacoco.Config := file("target/jacoco")

jacoco.reportFormats   in jacoco.Config := Seq(XMLReport("utf-8"), HTMLReport("utf-8"))

jacoco.excludes        in jacoco.Config := Seq("views*", "*Routes*", "controllers*routes*", "controllers*Reverse*", "controllers*javascript*", "controller*ref*")

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "4.0.2",
  "com.kenshoo" %% "metrics-play" % "2.3.0_0.1.6",
  cache
)
