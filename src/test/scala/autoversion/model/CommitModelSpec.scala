package autoversion.model

import org.scalatest.{FlatSpec, Matchers, OptionValues}
import sbtrelease.Version.Bump._

class CommitModelSpec extends FlatSpec with Matchers with OptionValues {

  val bugfixRegexes = List("""\[?bugfix\]?.*""", """\[?fix\]?.*""", """\[?patch\]?.*""").map(_.r)
  val minorRegexes = List(".*").map(_.r)
  val majorRegexes = List("""\[?breaking\]?.*""", """\[?major\]?.*""").map(_.r)

  it should "be a major bump" in {
    Commit("abcd1234", "[major] foo").suggestedBump(majorRegexes, minorRegexes, bugfixRegexes).value shouldEqual Major
  }

  it should "be a minor bump" in {
    Commit("abcd1234", "whatever").suggestedBump(majorRegexes, minorRegexes, bugfixRegexes).value shouldEqual Minor
  }

  it should "be a bugfix bump" in {
    Commit("abcd1234", "[bugfix]").suggestedBump(majorRegexes, minorRegexes, bugfixRegexes).value shouldEqual Bugfix
  }
}
