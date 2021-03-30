# sbt-autoversion

[![Travis branch](https://img.shields.io/travis/sbt/sbt-autoversion/master.svg)]()

The `sbt-autoversion` plugin builds on the [sbt-release](https://github.com/sbt/sbt-release) and [sbt-git](https://github.com/sbt/sbt-git) plugins to automatically manage the version bump to apply (major, minor or patch version bumps), based on commits messages patterns.

## Highlights

* Fully automatic releases
  * Supports `release with-defaults`
* Enforces [Semantic Versioning](https://semver.org/)
* Uses [Conventional Commits](https://www.conventionalcommits.org/) to determine version bumps
  * Configurable default behavior for unconventional commits

## Adding to your project

Add the following line to your `project/plugins.sbt`:

```scala
addSbtPlugin("org.scala-sbt" % "sbt-autoversion" % "1.0.0")
```

Since `sbt-autoversion` is an `AutoPlugin`, it will be automatically available to your projects.

## Usage

`sbt-autoversion` automatically wires itself in the setting of sbt-release's `releaseVersion` setting, meaning that you can use the sbt-release's `release with-defaults` command and use the non-interactive release process with the correct version configured.

`sbt-autoversion` however expose a few interesting tasks:

* `autoVersionLatestTag`: fetches the latest Git tag, based on Semantic Versioning ordering
* `autoVersionUnreleasedCommits`: lists commits since the latest tag/release
* `autoVersionSuggestedBump`: shows what version bump the plugin has computed and would automatically apply on the next release.

## Commits

Commits should be structured as follows:
```
<type>[optional scope]: <description>

[optional body]
```

See [Conventional Commits](https://www.conventionalcommits.org/) for more details.

## Settings

#### `autoVersionTagNameCleaner`

Linked to sbt-release's `releaseTagName` setting, defines how to "clean up" a Git tag to get back a semver-compatible version.

#### `autoVersionCommitConvention`

Selects a version bump based on the "type" of commit (see: [Conventional Commits](https://www.conventionalcommits.org)).

The following is the default convention:
```scala
case "major" | "breaking" => Some(Bump.Major)
case "minor" | "feat" | "feature" => Some(Bump.Minor)
case "fix" | "bugfix" | "patch" => Some(Bump.Bugfix)
case _ => None
```

Any commit with `!` in the `<type>` or `BREAKING CHANGE` in the description is also interpreted as a major version bump.

#### `autoVersionDefaultBump`

If the plugin is unable to suggest a version bump based on commit messages, this version bump will be suggested instead.
If set to `None`, an error will be thrown, and the release will be aborted.

Set to `Some(Bump.Bugfix)` by default.

# License

This software is under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).
