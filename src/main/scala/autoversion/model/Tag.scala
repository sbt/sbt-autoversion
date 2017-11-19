package autoversion.model

import com.vdurmont.semver4j.Semver

final case class Tag(raw: String, version: Semver)
