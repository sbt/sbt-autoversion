organization := "com.github.sbt"

sbtPlugin := true
crossSbtVersions := Vector("0.13.18", "1.2.7")

libraryDependencies += "com.vdurmont" % "semver4j" % "3.1.0"

libraryDependencies += "org.scalatest"  %% "scalatest"  % "3.2.2"  % "test"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.3" % "test"

// sbt plugin dependencies
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.6")
addSbtPlugin("com.typesafe.sbt"  % "sbt-git"     % "1.0.0")

scalacOptions := Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xfuture"
)

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))
