ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaProject"
  )

Compile / unmanagedJars ++=
  (file("C:/Bibliotecas") * "*.jar").classpath

