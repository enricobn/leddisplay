name := "leddisplay"

version := "1.0"

scalaVersion := "2.11.8"

enablePlugins(ScalaJSPlugin)

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.1"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.11.8"
)

// TEST
libraryDependencies += "com.lihaoyi" %%% "utest" % "0.4.3" % "test"
testFrameworks += new TestFramework("utest.runner.Framework")
