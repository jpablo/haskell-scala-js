
lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.1",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Python To Scala",
    tutSettings,
    tutScalacOptions := Seq("-Dscala.color")
  )
