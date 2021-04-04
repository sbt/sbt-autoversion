package autoversion.model

import org.scalatest.{FreeSpec, Matchers, OptionValues}

class ConventionalCommitSpec extends FreeSpec with Matchers with OptionValues {

  "unconventional commits" in {
    ConventionalCommit.parse("committed the thing to fix the other thing") shouldBe empty
    ConventionalCommit.parse("working on branch B") shouldBe empty
    ConventionalCommit.parse("fix stupidity") shouldBe empty
    ConventionalCommit.parse("asdf") shouldBe empty
    ConventionalCommit.parse("lorem ipsum dolor sit amet") shouldBe empty
    ConventionalCommit.parse("fix: ") shouldBe empty
    ConventionalCommit.parse(": foo") shouldBe empty
    ConventionalCommit.parse("(bar): foo") shouldBe empty
    ConventionalCommit.parse("(foo):") shouldBe empty
    ConventionalCommit.parse("(foo)") shouldBe empty
    ConventionalCommit.parse("") shouldBe empty
  }

  "conventional commits" - {

    "without scope" in {
      val commit = ConventionalCommit.parse("fix: do work").value
      commit.kind shouldEqual "fix"
      commit.breaking shouldBe false
      commit.scope shouldBe empty
      commit.description.value shouldEqual "do work"
    }

    "with scope" in {
      val commit = ConventionalCommit.parse("fix(services): do work").value
      commit.kind shouldEqual "fix"
      commit.breaking shouldBe false
      commit.scope.value shouldEqual "services"
      commit.description.value shouldEqual "do work"
    }

    "with multiword scope" in {
      val commit = ConventionalCommit.parse("fix(important 123 services): do work").value
      commit.kind shouldEqual "fix"
      commit.breaking shouldBe false
      commit.scope.value shouldEqual "important 123 services"
      commit.description.value shouldEqual "do work"
    }

    "with breaking flag" in {
      val commit = ConventionalCommit.parse("break(everything)!: oh no").value
      commit.kind shouldEqual "break"
      commit.breaking shouldBe true
      commit.scope.value shouldEqual "everything"
      commit.description.value shouldEqual "oh no"
    }

    "with BREAKING CHANGE in body" in {
      val commit = ConventionalCommit.parse("docs: introduce a BREAKING CHANGE").value
      commit.kind shouldEqual "docs"
      commit.breaking shouldBe true
      commit.scope shouldBe empty
      commit.description.value shouldEqual "introduce a BREAKING CHANGE"
    }

    "with BREAKING-CHANGE in body" in {
      val commit = ConventionalCommit.parse("docs: introduce a BREAKING-CHANGE").value
      commit.kind shouldEqual "docs"
      commit.breaking shouldBe true
      commit.scope shouldBe empty
      commit.description.value shouldEqual "introduce a BREAKING-CHANGE"
    }
  }
}
