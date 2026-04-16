lazy val scala212 = "2.12.21"
lazy val scala3   = "3.8.3"

ThisBuild / scalaVersion := scala212
ThisBuild / crossScalaVersions := Seq(scala212, scala3)

enablePlugins(SbtPlugin)

pluginCrossBuild / sbtVersion := {
  scalaBinaryVersion.value match {
    case "2.12" => "1.12.9"
    case _      => "2.0.0-RC12"
  }
}

// sbt 2 needs the full sbt version in plugin coordinates, but scripted on sbt 1
// still resolves plugins via the legacy sbt binary version ("1.0") layout.
projectID := {
  val base = ModuleID(organization.value, moduleName.value, version.value)
    .cross(crossVersion.value)
    .artifacts(artifacts.value: _*)
  scalaBinaryVersion.value match {
    case "2.12" => sbt.Defaults.sbtPluginExtra(base, sbtBinaryVersion.value, scalaBinaryVersion.value)
    case _ =>
      sbt.Defaults.sbtPluginExtra(
        base,
        (pluginCrossBuild / sbtVersion).value,
        (pluginCrossBuild / scalaBinaryVersion).value
      )
  }
}

libraryDependencies ++= Seq(
  "com.vdurmont"       % "semver4j"        % "3.1.0",
  "org.scalatest"     %% "scalatest"       % "3.2.19"   % Test,
  "org.scalacheck"    %% "scalacheck"      % "1.18.1"   % Test,
  "org.scalatestplus" %% "scalacheck-1-18" % "3.2.19.0" % Test
)

libraryDependencies ++= {
  val sbtV = (pluginCrossBuild / sbtVersion).value
  val scalaV = scalaBinaryVersion.value
  val sbtPluginCross = scalaV match {
    case "2.12" => "1.0"
    case _      => sbtV
  }
  Seq(
    sbt.Defaults.sbtPluginExtra("com.github.sbt" % "sbt-release" % "1.4.0", sbtPluginCross, scalaV),
    sbt.Defaults.sbtPluginExtra("com.github.sbt" % "sbt-git" % "2.1.0", sbtPluginCross, scalaV),
    sbt.Defaults.sbtPluginExtra("com.github.sbt" % "sbt2-compat" % "0.1.0", sbtPluginCross, scalaV)
  )
}

name := "sbt-autoversion"

scalacOptions := {
  val common = Seq(
    "-encoding",
    "UTF-8",
    "-deprecation",
    "-feature",
    "-unchecked"
  )
  scalaBinaryVersion.value match {
    case "2.12" => common :+ "-Xfuture"
    case _      => common
  }
}

scriptedBufferLog := false
scriptedLaunchOpts ++= Seq("-Xmx1024M", "-server", "-Dplugin.version=" + version.value)
