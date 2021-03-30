package autoversion

import autoversion.model.BumpOrdering.bumpOrdering
import autoversion.model.{Commit, ConventionalCommit, Tag}
import com.typesafe.sbt.{GitPlugin, SbtGit}
import com.vdurmont.semver4j.Semver
import com.vdurmont.semver4j.Semver.SemverType
import sbt._
import sbtrelease.ReleasePlugin
import sbtrelease.ReleasePlugin.autoImport.releaseVersion
import sbtrelease.Version.Bump

import scala.util.Properties

object AutoVersionPlugin extends AutoPlugin {

  val autoImport = Keys

  import autoImport._

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = GitPlugin && ReleasePlugin

  override def projectSettings: Seq[Setting[_]] = Seq(
    autoVersionLatestTag := findLatestTag.value,
    autoVersionTagNameCleaner := { _.stripPrefix("v") },
    autoVersionUnreleasedCommits := listUnreleasedCommits.value,
    autoVersionSuggestedBump := suggestBump.value,
    autoVersionCommitConvention := {
      case "major" | "breaking" => Some(Bump.Major)
      case "minor" | "feat" | "feature" => Some(Bump.Minor)
      case "fix" | "bugfix" | "patch" => Some(Bump.Bugfix)
      case _ => None
    },
    autoVersionDefaultBump := Some(Bump.Bugfix),
    releaseVersion := AutoVersion.setReleaseVersion(autoVersionSuggestedBump.value)
  )

  private lazy val findLatestTag = Def.task {
    val gitTags = runGit("tag", "--list").value
    val versions = gitTags.map(tag => Tag(tag, new Semver(autoVersionTagNameCleaner.value(tag), SemverType.LOOSE)))
    versions.sortBy(_.version).lastOption
  }

  private lazy val listUnreleasedCommits = Def.taskDyn {
    val tag = autoVersionLatestTag.value.map(tag => s"${tag.raw}...").getOrElse("")
    Def.task {
      val commitListOutput = runGit("log", "--oneline", "--no-decorate", "--color=never", s"${tag}HEAD").value
      commitListOutput.map(Commit.apply).toVector
    }
  }

  private lazy val suggestBump = Def.task {
    val log = sbt.Keys.streams.value.log
    val default = autoVersionDefaultBump.value

    val suggestedBumps = {
      val commits = autoVersionUnreleasedCommits.value
      commits
        .flatMap(commit => ConventionalCommit.parse(commit.msg))
        .flatMap { commit =>
          if (commit.breaking) Some(Bump.Major)
          else autoVersionCommitConvention.value(commit.kind)
        }
    }

    if (suggestedBumps.nonEmpty) suggestedBumps.max
    else default match {
      case None => sys.error("Could not identify any commits that adhere to configured sbt-autoversion convention!")
      case Some(bump) =>
        log.warn(
          "Unreleased commits did not adhere to configured sbt-autoversion convention. " +
            s"Defaulting to '${bump.toString}'."
        )
        bump
    }
  }

  private def runGit(args: String*) = Def.task {
    SbtGit.GitKeys.gitRunner
      .value(args: _*)(file("."), Logger.Null)
      .split(Properties.lineSeparator)
      .filter(_.trim.nonEmpty)
  }
}
