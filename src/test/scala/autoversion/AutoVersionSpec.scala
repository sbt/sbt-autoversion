package autoversion

import org.scalatest.{FreeSpec, Matchers}
import sbtrelease.Version.Bump
import sbtrelease.{Version, versionFormatError}

class AutoVersionSpec extends FreeSpec with Matchers {

  private def autoVersion(raw: String, bump: Bump): String = {
    val version = Version(raw).getOrElse(versionFormatError(raw))
    AutoVersion.setReleaseVersion(bump)(version.string)
  }

  "qualified patch release" - {
    val version = "3.9.18-SNAPSHOT"

    "patch: drop the qualifier" in {
      autoVersion(version, Bump.Bugfix) shouldEqual "3.9.18"
    }

    "minor: proceed to next minor" in {
      autoVersion(version, Bump.Minor) shouldEqual "3.10.0"
    }

    "major: proceed to next major" in {
      autoVersion(version, Bump.Major) shouldEqual "4.0.0"
    }
  }

  "unqualified patch release" - {
    val version = "3.9.18"

    "patch: proceed to next patch" in {
      autoVersion(version, Bump.Bugfix) shouldEqual "3.9.19"
    }

    "minor: proceed to next minor" in {
      autoVersion(version, Bump.Minor) shouldEqual "3.10.0"
    }

    "major: proceed to next major" in {
      autoVersion(version, Bump.Major) shouldEqual "4.0.0"
    }
  }

  "overly precise version" - {
    val version = "3.9.18.4.12"

    "patch: proceed to next patch" in {
      autoVersion(version, Bump.Bugfix) shouldEqual "3.9.19.0.0"
    }

    "minor: proceed to next minor" in {
      autoVersion(version, Bump.Minor) shouldEqual "3.10.0.0.0"
    }

    "major: proceed to next major" in {
      autoVersion(version, Bump.Major) shouldEqual "4.0.0.0.0"
    }
  }
}
