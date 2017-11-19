# sbt-autoversion

[![Travis branch](https://img.shields.io/travis/sbt/sbt-autoversion/master.svg)]()

The `sbt-autoversion` plugin builds on the [sbt-release](https://github.com/sbt/sbt-release) and [sbt-git](https://github.com/sbt/sbt-git) plugins to automatically manage the version bump to apply (major, minor or patch version bumps), based on commits messages patterns.

## Adding to your project

Add the following line to your `project/plugins.sbt`:

```scala
addSbtPlugin("org.scala-sbt" % "sbt-autoversion" % "1.0.0")
```

Since `sbt-autoversion` is an AutoPlugin, it will be automatically available to your projects,
given you're including both the [sbt-release](https://github.com/sbt/sbt-release) and [sbt-git](https://github.com/sbt/sbt-git) plugins.

## Usage

`sbt-autoversion` automatically wires itself in the setting of sbt-release's `releaseVersion` setting, meaning that you can use the sbt-release's `release with-defaults` command and use the non-interactive release process with the correct version configured.

`sbt-autoversion` however expose a few interesting tasks:

* `latestTag`: fetches the latest Git tag, based on Semantic Versioning ordering
* `unreleasedCommits`: lists commits since the latest tag/release
* `suggestedBump`: shows what version bump the plugin has computed and would automatically apply on the next release.

## Settings

#### `tagNameCleaner`

Linked to sbt-release's `releaseTagName` setting, defines how to "clean up" a Git tag to get back a semver-compatible version.

#### `majorRegexes`, `minorRegexes`, `bugfixRegexes`

The list of regular expression that a commit message should match to be seen as requiring respectively a major, a minor or a bugfix version bump (must match at least one pattern).

Default patterns:

* major: `\[?breaking\]?.*` `\[?major\]?.*`
* minor: `.*`
* bugfix: `\[?bugfix\]?.*`, `\[?fix\]?.*`

# License

This software is under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).