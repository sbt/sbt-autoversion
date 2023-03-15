package autoversion

import autoversion.model.{Commit, Tag}
import sbt.{settingKey, taskKey}
import sbtrelease.Version.Bump

import scala.util.matching.Regex

object Keys {
  val latestTag         = taskKey[Option[Tag]]("Latest semver version from Git tags.")
  val tagNameCleaner    = settingKey[String => String]("Cleans the git tag to extract only the version.")
  val unreleasedCommits = taskKey[Seq[Commit]]("Commits since the latest tagged release.")
  val suggestedBump     = taskKey[Bump]("Version bump computed by sbt-autoversion")
  val nanoRegexes       = settingKey[Seq[Regex]]("Regex that commit messages must follow to use a nano bump.")
  val bugfixRegexes     = settingKey[Seq[Regex]]("Regex that commit messages must follow to use a bugfix bump.")
  val minorRegexes      = settingKey[Seq[Regex]]("Regex that commit messages must follow to use a minor bump.")
  val majorRegexes      = settingKey[Seq[Regex]]("Regex that commit messages must follow to use a major bump.")
  val defaultBump = settingKey[Option[Bump]](
    "Default version bump if sbt-autoversion is unable to suggest one based on commit messages."
  )
  val conventionalPatternsAdditive = settingKey[Boolean](
    "A flag for controlling whether Conventional Commits are applied additionally to the default patterns, or override them completely.  Defaults to true"
  )
}
