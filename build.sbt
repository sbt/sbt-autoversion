enablePlugins(SbtPlugin)

libraryDependencies += "com.vdurmont" % "semver4j" % "3.1.0"

libraryDependencies += "org.scalatest"  %% "scalatest"  % "3.0.9"  % "test"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.16.0" % "test"

// sbt plugin dependencies
addSbtPlugin("com.github.sbt"   % "sbt-release" % "1.0.15")
addSbtPlugin("com.typesafe.sbt" % "sbt-git"     % "1.0.1")

scalacOptions := Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xfuture"
)

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

scriptedBufferLog := false
scriptedLaunchOpts ++= Seq("-Xmx1024M", "-server", "-Dplugin.version=" + version.value)
