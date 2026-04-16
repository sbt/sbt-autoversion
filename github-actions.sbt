inThisBuild(
  Seq(
    githubWorkflowJavaVersions ++= Seq(JavaSpec.temurin("11"), JavaSpec.temurin("17"), JavaSpec.temurin("21")),
    githubWorkflowBuild := Seq(WorkflowStep.Sbt(List("test", "scripted"))),
    githubWorkflowBuildMatrixExclusions ++= Seq(
      MatrixExclude(Map("scala" -> "3.8.3", "java" -> "zulu@8")),
      MatrixExclude(Map("scala" -> "3.8.3", "java" -> "temurin@11")),
    ),
    githubWorkflowTargetTags ++= Seq("v*"),
    githubWorkflowPublishTargetBranches += RefPredicate.StartsWith(Ref.Tag("v")),
    githubWorkflowPublish := Seq(
      WorkflowStep.Sbt(
        List("ci-release"),
        env = Map(
          "PGP_PASSPHRASE"    -> "${{ secrets.PGP_PASSPHRASE }}",
          "PGP_SECRET"        -> "${{ secrets.PGP_SECRET }}",
          "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
          "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
        )
      )
    )
  )
)
