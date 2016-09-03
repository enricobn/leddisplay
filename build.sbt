name := "led_display"

version := "1.0"

scalaVersion := "2.11.8"
//scalaVersion := "2.10.4"

enablePlugins(ScalaJSPlugin)

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.0"

// REACT

// core = essentials only. No bells or whistles.
libraryDependencies += "com.github.japgolly.scalajs-react" %%% "core" % "0.11.1"

// React JS itself (Note the filenames, adjust as needed, eg. to remove addons.)
jsDependencies ++= Seq(

  "org.webjars.bower" % "react" % "15.2.1"
    /        "react-with-addons.js"
    minified "react-with-addons.min.js"
    commonJSName "React",

  "org.webjars.bower" % "react" % "15.2.1"
    /         "react-dom.js"
    minified  "react-dom.min.js"
    dependsOn "react-with-addons.js"
    commonJSName "ReactDOM",

  "org.webjars.bower" % "react" % "15.2.1"
    /         "react-dom-server.js"
    minified  "react-dom-server.min.js"
    dependsOn "react-dom.js"
    commonJSName "ReactDOMServer")

// JSON

libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.4.1"