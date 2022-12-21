package autoversion

import sbtrelease.Version.Bump
import sbtrelease.{versionFormatError, Version}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AutoVersionSpec extends AnyFlatSpec with Matchers {

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

  "A nano bump on a qualified version" should "proceed to next nano" in {
    autoVersion(unqualifiedRelease, Bump.Nano) shouldEqual "3.9.18.1"
  }

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

  "A nano bump on an overly precise version" should "proceed to next nano" in {
    autoVersion(overlyPreciseVersion, Bump.Nano) shouldEqual "3.9.18.5.0"
  }

  "A patch bump on an overly precise version" should "proceed to next patch" in {
    autoVersion(overlyPreciseVersion, Bump.Bugfix) shouldEqual "3.9.19.0.0"
  }

  "A patch bump on an overly precise SNAPSHOT version" should "proceed to next patch" in {
    autoVersion("2.0.0.2-SNAPSHOT", Bump.Bugfix) shouldEqual "2.0.1.0"
  }

  "A nano bump on an overly precise SNAPSHOT version" should "proceed to next nano" in {
    autoVersion("2.0.0.2-SNAPSHOT", Bump.Nano) shouldEqual "2.0.0.2"
  }

  "A minor bump on an overly precise version" should "proceed to next minor" in {
    autoVersion(overlyPreciseVersion, Bump.Minor) shouldEqual "3.10.0.0.0"
  }

  "A major bump on an overly precise version" should "proceed to next major" in {
    autoVersion(overlyPreciseVersion, Bump.Major) shouldEqual "4.0.0.0.0"
  }

  private val emptyInitialVersion = "0.0.0"

  "A patch bump on an empty version" should "proceed to next patch" in {
    autoVersion(emptyInitialVersion, Bump.Bugfix) shouldEqual "0.0.1"
  }

  "A minor bump on an empty version" should "proceed to next minor" in {
    autoVersion(emptyInitialVersion, Bump.Minor) shouldEqual "0.1.0"
  }

  "A major bump on an empty version" should "proceed to next major" in {
    autoVersion(emptyInitialVersion, Bump.Major) shouldEqual "1.0.0"
  }
}
