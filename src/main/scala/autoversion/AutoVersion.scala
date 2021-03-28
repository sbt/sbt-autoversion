package autoversion

import sbtrelease.Version.Bump
import sbtrelease.{Version, versionFormatError}

object AutoVersion {

  def setReleaseVersion(bump: Bump): String => String = { (ver: String) =>
    val bugfixSuggested = bump == Bump.Bugfix

    // If the previous version is a snapshot (qualified) and a bugfix is recommended, simply drop the qualifier.
    // Otherwise the expected bugfix version will be skipped.
    // e.g. 0.1.4 => 0.1.5-SNAPSHOT => 0.1.5
    Version(ver).map {
      case v if v.qualifier.isDefined && bugfixSuggested => v.withoutQualifier.string
      case v => v.bump(bump).withoutQualifier.string
    }.getOrElse(versionFormatError(ver))
  }
}
