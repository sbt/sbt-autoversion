import ReleaseTransformations._

Global / onChangedBuildSource := ReloadOnSourceChanges

// Default sbt-release process, minus publication (for testing).
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  // publishArtifacts,
  setNextVersion,
  commitNextVersion
  // pushChanges
)
