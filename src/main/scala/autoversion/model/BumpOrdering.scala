package autoversion.model

import sbtrelease.Version.Bump

object BumpOrdering {
  implicit val bumpOrdering: Ordering[Bump] = new Ordering[Bump] {
    override def compare(x: Bump, y: Bump): Int =
      if (x == y) 0
      else
        (x, y) match {
          case (_, Bump.Major)           => -1
          case (Bump.Major, _)           => 1
          case (Bump.Bugfix, Bump.Minor) => -1
          case (Bump.Minor, Bump.Bugfix) => 1
          case (Bump.Nano, _)            => -1
          case (_, Bump.Nano)            => 1
          case _                         => throw new RuntimeException(s"Unhandled case: ($x,$y)")
        }
  }
}
