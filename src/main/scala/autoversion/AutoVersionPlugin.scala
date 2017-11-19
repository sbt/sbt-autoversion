package autoversion

import autoversion.model.{Commit, Tag}
import autoversion.model.BumpOrdering.bumpOrdering

import sbt._
import com.typesafe.sbt.{GitPlugin, SbtGit}
import com.vdurmont.semver4j.Semver
import com.vdurmont.semver4j.Semver.SemverType
import sbtrelease.{versionFormatError, ReleasePlugin, Version}
import sbtrelease.ReleasePlugin.autoImport.releaseVersion

import scala.util.Properties

object AutoVersionPlugin extends AutoPlugin {

  val autoImport = Keys

  import autoImport._

  override def trigger: PluginTrigger = allRequirements
  override def requires: Plugins      = GitPlugin && ReleasePlugin

  override def projectSettings: Seq[Setting[_]] = Seq(
    tagNameCleaner := { _.stripPrefix("v") },
    bugfixRegexes := List("""\[?bugfix\]?.*""", """\[?fix\]?.*""", """\[?patch\]?.*""").map(_.r),
    minorRegexes := List(".*").map(_.r),
    majorRegexes := List("""\[?breaking\]?.*""", """\[?major\]?.*""").map(_.r),
    latestTag := findLatestTag.value,
    unreleasedCommits := listUnreleasedCommits.value,
    suggestedBump := suggestBump.value,
    releaseVersion := { ver =>
      Version(ver).map(v => v.bump(suggestedBump.value).withoutQualifier.string).getOrElse(versionFormatError)
    }
  )

  private lazy val findLatestTag = Def.task {
    val gitTags  = runGit("tag", "--list").value
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
    val commits = unreleasedCommits.value
    val suggestedBumps =
      commits.flatMap(_.suggestedBump(majorRegexes.value, minorRegexes.value, bugfixRegexes.value))
    if (suggestedBumps.isEmpty)
      throw new RuntimeException("No commit matches either patterns for bugfix, minor or major bumps !")
    else suggestedBumps.max
  }

  private def runGit(args: String*) = Def.task {
    SbtGit.GitKeys.gitRunner
      .value(args: _*)(file("."), Logger.Null)
      .split(Properties.lineSeparator)
      .filter(_.trim.nonEmpty)
  }
}
