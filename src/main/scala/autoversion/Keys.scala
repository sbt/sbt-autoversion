package autoversion

import autoversion.model.{Commit, Tag}
import sbt.{settingKey, taskKey}
import sbtrelease.Version.Bump

import scala.util.matching.Regex

object Keys {
  val autoVersionLatestTag         = taskKey[Option[Tag]]("Latest semver version from Git tags.")
  val autoVersionTagNameCleaner    = settingKey[String => String]("Cleans the git tag to extract only the version.")
  val autoVersionUnreleasedCommits = taskKey[Seq[Commit]]("Commits since the latest tagged release.")
  val autoVersionSuggestedBump     = taskKey[Bump]("Version bump computed by sbt-autoversion")
  val autoVersionBugfixRegexes     = settingKey[Seq[Regex]]("Regex that commit messages must follow to use a bugfix bump.")
  val autoVersionMinorRegexes      = settingKey[Seq[Regex]]("Regex that commit messages must follow to use a minor bump.")
  val autoVersionMajorRegexes      = settingKey[Seq[Regex]]("Regex that commit messages must follow to use a major bump.")
  val autoVersionDefaultBump       = settingKey[Option[Bump]]("Default version bump if sbt-autoversion is unable to suggest one based on commit messages.")
}
