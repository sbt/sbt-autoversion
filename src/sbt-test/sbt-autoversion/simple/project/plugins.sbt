{
  val pluginVersion = System.getProperty("plugin.version")
  if (pluginVersion == null || pluginVersion.isBlank)
    throw new RuntimeException("The system property 'plugin.version' is not defined.")
  else addSbtPlugin("com.github.sbt" % "sbt-autoversion" % pluginVersion)
}


