# sbt-autoversion

[![Travis branch](https://img.shields.io/travis/sbt/sbt-autoversion/master.svg)]()

The `sbt-autoversion` plugin builds on the [sbt-release](https://github.com/sbt/sbt-release) and [sbt-git](https://github.com/sbt/sbt-git) plugins to automatically manage the version bump to apply (major, minor or patch version bumps), based on commits messages patterns.

## Adding to your project

Add the following line to your `project/plugins.sbt`:

```scala
addSbtPlugin("me.pdalpra" % "sbt-autoversion" % "2.0.0")
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

The list of regular expression that a commit message should match to be seen as requiring respectively a major, a minor, or a bugfix version bump.

Default patterns:

* major: `\[?breaking\]?.*`, `\[?major\]?.*`
* minor: `\[?feature\]?.*`, `\[?minor\]?.*`
* bugfix: `\[?bugfix\]?.*`, `\[?fix\]?.*`

Note: regular expressions are executed in the order shown above (major, minor, then bugfix) and the first match is returned.
See `defaultBump` for behavior if no matches are found in the unreleased commit messages.

#### `defaultBump`

If the plugin is unable to suggest a version bump based on commit messages (i.e., if none of the configured regular expressions match), this version bump will be suggested instead.
If set to `None`, an error will be thrown, and the release will be aborted.

Set to `Some(Bump.Bugfix)` by default.

#### `unreleasedCommits`

By default, `git log --oneline` is used to list the unreleased commits and match them against the regexes. If you need
the full commit text (e.g. to search for scala-steward semver labels), you can use
* `unreleasedCommits := AutoVersionPlugin.listUnreleasedCommitsLong.value`

# License

This software is under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).
