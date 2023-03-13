package autoversion.conventional

import autoversion.AutoVersionPlugin
import autoversion.Keys.{bugfixRegexes, majorRegexes, minorRegexes}
import sbt.{AutoPlugin, Setting}

/**
 * An additional opt-in plugin that enables Conventional Commit style patterns for bumping the version
 */
object ConventionalCommits extends AutoPlugin {
  override def requires = AutoVersionPlugin

  override def projectSettings: Seq[Setting[_]] =
    Seq(
      majorRegexes := Seq(""".*BREAKING[-\s]CHANGE: .*""".r, "^(.*!: ).*".r),
      minorRegexes := Seq("^feat.*: .*".r),
      bugfixRegexes := Seq("^fix.*: .*".r),
    )
}
