lazy val root = project.in(file(".")).enablePlugins(PlayJava)

name := """field-research-webservice"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"

libraryDependencies += "org.sql2o" % "sql2o" % "1.5.4"