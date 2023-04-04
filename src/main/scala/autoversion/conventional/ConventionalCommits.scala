package autoversion.conventional

import autoversion.AutoVersionPlugin
import autoversion.Keys.{bugfixRegexes, conventionalPatternsAdditive, majorRegexes, minorRegexes}
import sbt.{settingKey, AutoPlugin, Setting}

/** An additional opt-in plugin that enables Conventional Commit style patterns for bumping the version
  */
object ConventionalCommits extends AutoPlugin {

  override def requires = AutoVersionPlugin

  override def projectSettings: Seq[Setting[_]] =
    Seq(
      conventionalPatternsAdditive := true,
      majorRegexes := {
        val conventionalMajor = Seq(""".*BREAKING[-\s]CHANGE: .*""".r, "^(.*!: ).*".r)
        if (conventionalPatternsAdditive.value)
          conventionalMajor ++ majorRegexes.value
        else
          conventionalMajor
      },
      minorRegexes := {
        val conventionalMinor = Seq("^feat.*: .*".r)
        if (conventionalPatternsAdditive.value)
          conventionalMinor ++ minorRegexes.value
        else
          conventionalMinor
      },
      bugfixRegexes := {
        val conventionalBugfix = Seq("^feat.*: .*".r)
        if (conventionalPatternsAdditive.value)
          conventionalBugfix ++ bugfixRegexes.value
        else
          conventionalBugfix
      }
    )
}
