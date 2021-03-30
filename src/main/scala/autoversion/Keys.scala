package autoversion

import autoversion.model.{Commit, ConventionalCommit, Tag}
import sbt.{settingKey, taskKey}
import sbtrelease.Version.Bump

object Keys {
  val autoVersionLatestTag         = taskKey[Option[Tag]]("Latest semver version from Git tags.")
  val autoVersionTagNameCleaner    = settingKey[String => String]("Cleans the git tag to extract only the version.")
  val autoVersionUnreleasedCommits = taskKey[Seq[Commit]]("Commits since the latest tagged release.")
  val autoVersionSuggestedBump     = taskKey[Bump]("Version bump computed by sbt-autoversion")
  val autoVersionCommitConvention  = settingKey[String => Option[Bump]]("Map allowed commit types to their expected version bump.")
  val autoVersionDefaultBump       = settingKey[Option[Bump]]("Default version bump if sbt-autoversion is unable to suggest one based on commit messages.")
}
