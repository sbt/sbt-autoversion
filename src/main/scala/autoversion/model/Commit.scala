package autoversion.model

import sbtrelease.Version.Bump

import scala.util.matching.Regex

object Commit {
  def apply(commitLine: String): Commit = {
    def parts = commitLine.split(" ")
    Commit(parts(0), parts.drop(1).mkString(" "))
  }
}
case class Commit(sha: String, msg: String) {
  def suggestedBump(
      majorRegexes: Seq[Regex],
      minorRegexes: Seq[Regex],
      bugfixRegexes: Seq[Regex],
      nanoRegexes: Seq[Regex]
  ): Option[Bump] =
    Seq((majorRegexes, Bump.Major), (minorRegexes, Bump.Minor), (bugfixRegexes, Bump.Bugfix), (nanoRegexes, Bump.Nano))
      .collectFirst {
        case (regexes, bump) if regexes.exists(r => matches(r, msg)) => bump
      }

  private def matches(regex: Regex, s: String): Boolean =
    regex.pattern.matcher(s).matches
}
