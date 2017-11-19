// Bintray

publishMavenStyle := false
bintrayRepository := "sbt-plugins"
bintrayOrganization in bintray := None
bintrayReleaseOnPublish := false

// Release
import ReleaseTransformations._
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  releaseStepCommandAndRemaining("^ test"),
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("^ publish"),
  releaseStepTask(bintrayRelease),
  setNextVersion,
  commitNextVersion,
  pushChanges
)
