package autoversion

import sbtrelease.Version.Bump
import sbtrelease.{versionFormatError, Version}

object AutoVersion {

  def setReleaseVersion(bump: Bump): String => String = { (ver: String) =>
    val bugfixSuggested = bump == Bump.Bugfix
    val nanoSuggested   = bump == Bump.Nano

    // If the previous version is a snapshot (qualified) and a bugfix is recommended, simply drop the qualifier.
    // Otherwise the expected bugfix version will be skipped.
    // e.g. 0.1.4 => 0.1.5-SNAPSHOT => 0.1.5
    // same applies for nano:
    // e.g. 0.1.4.5 => 0.1.4.6-SNAPSHOT => 0.1.4.6
    Version(ver)
      .map {
        case v if v.qualifier.isDefined && bugfixSuggested && v.subversions.size <= 2 => v.withoutQualifier
        case v if nanoSuggested && v.subversions.size < 3 =>
          val versionWithNano = v.copy(subversions = v.subversions ++ Seq.fill(3 - v.subversions.size)(0))
          versionWithNano.bump(bump).withoutQualifier
        case v if v.qualifier.isDefined && nanoSuggested => v.withoutQualifier
        case v                                           => v.bump(bump).withoutQualifier
      }
      .map(_.string)
      .getOrElse(versionFormatError(ver))
  }
}
