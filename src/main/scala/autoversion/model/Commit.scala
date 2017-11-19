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
  def suggestedBump(majorRegexes: Seq[Regex], minorRegexes: Seq[Regex], bugfixRegexes: Seq[Regex]): Option[Bump] = {
    val majorSuggested = majorRegexes.exists(r => matches(r, msg))
    if (majorSuggested) Some(Bump.Major)
    else {
      val minorSuggested = minorRegexes.exists(r => matches(r, msg))
      if (minorSuggested) Some(Bump.Minor)
      else {
        val bugfixSuggested = bugfixRegexes.exists(r => matches(r, msg))
        if (bugfixSuggested) Some(Bump.Bugfix)
        else None
      }
    }
  }

  private def matches(regex: Regex, s: String): Boolean =
    regex.pattern.matcher(s).matches
}
