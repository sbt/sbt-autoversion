enablePlugins(SbtPlugin)

libraryDependencies ++= Seq(
  "com.vdurmont"       % "semver4j"        % "3.1.0",
  "org.scalatest"     %% "scalatest"       % "3.2.13"   % Test,
  "org.scalacheck"    %% "scalacheck"      % "1.17.0"   % Test,
  "org.scalatestplus" %% "scalacheck-1-16" % "3.2.13.0" % Test
)

// sbt plugin dependencies

addSbtPlugin("com.github.sbt" % "sbt-release" % "1.1.0")
addSbtPlugin("com.github.sbt" % "sbt-git"     % "2.0.0")

scalacOptions := Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xfuture"
)
