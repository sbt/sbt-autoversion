package autoversion

import autoversion.model.BumpOrdering.bumpOrdering
import autoversion.model.{Commit, Tag}
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
    tagNameCleaner := { _.stripPrefix("v") },
    bugfixRegexes := List("""\[?(bug)?fix\]?.*""", """\[?patch\]?.*""").map(_.r),
    minorRegexes := List("""\[?feature\]?.*""", """\[?minor\]?.*""").map(_.r),
    majorRegexes := List("""\[?breaking\]?.*""", """\[?major\]?.*""").map(_.r),
    latestTag := findLatestTag.value,
    unreleasedCommits := listUnreleasedCommits.value,
    suggestedBump := suggestBump.value,
    releaseVersion := AutoVersion.setReleaseVersion(suggestedBump.value),
    defaultBump := Some(Bump.Bugfix)
  )

  private lazy val findLatestTag = Def.task {
    val gitTags = runGit("tag", "--list").value
    val versions = gitTags.map(tag => Tag(tag, new Semver(tagNameCleaner.value(tag), SemverType.LOOSE)))
    versions.sortBy(_.version).lastOption
  }

  private lazy val listUnreleasedCommits = Def.taskDyn {
    val tag = latestTag.value.map(tag => s"${tag.raw}...").getOrElse("")
    Def.task {
      val commitListOutput = runGit("log", "--oneline", "--no-decorate", "--color=never", s"${tag}HEAD").value
      commitListOutput.map(Commit.apply).toVector
    }
  }

  private lazy val suggestBump = Def.task {
    val log = sbt.Keys.streams.value.log
    val default = defaultBump.value
    val suggestedBumps = {
      val commits = unreleasedCommits.value
      commits.flatMap(_.suggestedBump(majorRegexes.value, minorRegexes.value, bugfixRegexes.value))
    }

    if (suggestedBumps.nonEmpty) suggestedBumps.max
    else default match {
      case None => sys.error("No commit matches either patterns for bugfix, minor or major bumps !")
      case Some(bump) =>
        log.warn(
          "Unreleased commits did not match any configured sbt-autoversion regular expression. " +
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
