package autoversion.model

case class ConventionalCommit(
  commitMessage: String,
  kind: String,
  breaking: Boolean = false,
  scope: Option[String] = None,
  description: Option[String] = None
)

object ConventionalCommit {

  def parse(message: String): Option[ConventionalCommit] = {
    val pattern = """^(?<kind>[\w\s]+)\s*(\((?<scope>[\w\s]+)\))?\s*(?<breaking>!)?\s*:\s+(?<description>.+)$"""
    val regex = pattern.r("kind", "outerscope", "scope", "breaking", "description")

    regex.findFirstMatchIn(message).flatMap { m =>
      val breaking = Option(m.group("breaking")).isDefined || message.contains("BREAKING CHANGE")

      for {
        kind <- Option(m.group("kind"))
      } yield ConventionalCommit(
        commitMessage = message,
        kind = kind.trim.toLowerCase,
        breaking = breaking,
        scope = Option(m.group("scope")).map(_.trim.toLowerCase),
        description = Option(m.group("description")).map(_.trim)
      )
    }
  }

}
