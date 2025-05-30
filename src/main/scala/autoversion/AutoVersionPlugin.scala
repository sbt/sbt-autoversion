package autoversion

import autoversion.model.BumpOrdering.bumpOrdering
import autoversion.model.{Commit, Tag}
import com.github.sbt.git.{GitPlugin, SbtGit}
import com.vdurmont.semver4j.Semver
import com.vdurmont.semver4j.Semver.SemverType
import sbt.{file, AutoPlugin, Def, Logger, PluginTrigger, Plugins, Setting}
import sbtrelease.ReleasePlugin
import sbtrelease.ReleasePlugin.autoImport.releaseVersion
import sbtrelease.Version.Bump

import scala.util.Properties

object AutoVersionPlugin extends AutoPlugin {

  val autoImport = Keys

  import autoImport.*

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = GitPlugin && ReleasePlugin

  override def projectSettings: Seq[Setting[_]] =
    Seq(
      tagNameCleaner    := { _.stripPrefix("v") },
      nanoRegexes       := List("""\[?nano\]?.*""").map(_.r),
      bugfixRegexes     := List("""\[?(bug)?fix\]?.*""", """\[?patch\]?.*""").map(_.r),
      minorRegexes      := List("""\[?feature\]?.*""", """\[?minor\]?.*""").map(_.r),
      majorRegexes      := List("""\[?breaking\]?.*""", """\[?major\]?.*""").map(_.r),
      latestTag         := findLatestTag.value,
      unreleasedCommits := listUnreleasedCommits.value,
      suggestedBump     := suggestBump.value,
      releaseVersion    := AutoVersion.setReleaseVersion(suggestedBump.value),
      defaultBump       := Some(Bump.Bugfix)
    )

  private lazy val findLatestTag = Def.task {
    // uses git describe to find the closest reachable tag belonging to the tree of current HEAD with a prefix of 'v'
    runGit("describe", "--abbrev=0", "--always", "--match=v*").value
      .filter(_.startsWith("v"))
      .map(tag => Tag(tag, new Semver(tagNameCleaner.value(tag), SemverType.LOOSE)))
      .headOption
  }

  private lazy val listUnreleasedCommits = Def.taskDyn {
    val tag = latestTag.value.map(tag => s"${tag.raw}...").getOrElse("")
    Def.task {
      val commitListOutput = runGit("log", "--oneline", "--no-decorate", "--color=never", s"${tag}HEAD").value
      commitListOutput.map(Commit.apply).toVector
    }
  }

  private lazy val suggestBump = Def.task {
    val log            = sbt.Keys.streams.value.log
    val default        = defaultBump.value
    val suggestedBumps = {
      val commits = unreleasedCommits.value
      commits.flatMap(_.suggestedBump(majorRegexes.value, minorRegexes.value, bugfixRegexes.value, nanoRegexes.value))
    }

    if (suggestedBumps.nonEmpty) suggestedBumps.max
    else
      default match {
        case None =>
          throw new RuntimeException("No commit matches either patterns for bugfix, nano, minor or major bumps !")
        case Some(bump) =>
          log.warn(
            "Unreleased commits did not match any configured sbt-autoversion regular expression. " +
              s"Defaulting to '${bump.toString}'."
          )
          bump
      }
  }

  private def runGit(args: String*) =
    Def.task {
      SbtGit.GitKeys.gitRunner
        .value(args: _*)(file("."), Logger.Null)
        .split(Properties.lineSeparator)
        .filter(_.trim.nonEmpty)
    }
}
