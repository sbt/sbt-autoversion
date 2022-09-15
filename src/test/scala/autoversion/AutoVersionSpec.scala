package autoversion

import org.scalatest.{FlatSpec, Matchers}
import sbtrelease.Version.Bump
import sbtrelease.{versionFormatError, Version}

class AutoVersionSpec extends FlatSpec with Matchers {

  private def autoVersion(raw: String, bump: Bump): String = {
    val version = Version(raw).getOrElse(versionFormatError(raw))
    AutoVersion.setReleaseVersion(bump)(version.string)
  }

  private val qualifiedRelease = "3.9.18-SNAPSHOT"

  "A patch bump on a qualified version" should "drop the qualifier" in {
    autoVersion(qualifiedRelease, Bump.Bugfix) shouldEqual "3.9.18"
  }

  "A minor bump on a qualified version" should "proceed to next minor" in {
    autoVersion(qualifiedRelease, Bump.Minor) shouldEqual "3.10.0"
  }

  "A major bump on a qualified version" should "proceed to next major" in {
    autoVersion(qualifiedRelease, Bump.Major) shouldEqual "4.0.0"
  }

  private val unqualifiedRelease = "3.9.18"

  "A patch bump on an unqualified version" should "proceed to next patch" in {
    autoVersion(unqualifiedRelease, Bump.Bugfix) shouldEqual "3.9.19"
  }

  "A minor bump on an unqualified version" should "proceed to next minor" in {
    autoVersion(unqualifiedRelease, Bump.Minor) shouldEqual "3.10.0"
  }

  "A major bump on an unqualified version" should "proceed to next major" in {
    autoVersion(unqualifiedRelease, Bump.Major) shouldEqual "4.0.0"
  }

  private val overlyPreciseVersion = "3.9.18.4.12"

  "A patch bump on an overly precise version" should "proceed to next patch" in {
    autoVersion(overlyPreciseVersion, Bump.Bugfix) shouldEqual "3.9.19.0.0"
  }

  "A minor bump on an overly precise version" should "proceed to next minor" in {
    autoVersion(overlyPreciseVersion, Bump.Minor) shouldEqual "3.10.0.0.0"
  }

  "A major bump on an overly precise version" should "proceed to next major" in {
    autoVersion(overlyPreciseVersion, Bump.Major) shouldEqual "4.0.0.0.0"
  }
}
