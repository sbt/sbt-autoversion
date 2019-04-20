organization := "com.github.sbt"

sbtPlugin := true
crossSbtVersions := Vector("0.13.18", "1.2.7")

libraryDependencies += "com.vdurmont" % "semver4j" % "2.0.2"

libraryDependencies += "org.scalatest"  %% "scalatest"  % "3.0.5"  % "test"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"

// sbt plugin dependencies
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.6")
addSbtPlugin("com.typesafe.sbt"  % "sbt-git"     % "0.9.3")

scalacOptions := Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xfuture"
)

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))
