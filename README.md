sbt-izpack
==========

<a href="https://raw.githubusercontent.com/MediaCluster/sbt-izpack/master/LICENSE">
  <img src="https://img.shields.io/hexpm/l/plug.svg" alt="License: Apache 2">
</a>
<a href='https://bintray.com/mediacluster/sbt-plugins/sbt-izpack/1.1.0/link'>
  <img src='https://api.bintray.com/packages/mediacluster/sbt-plugins/sbt-izpack/images/download.svg?version=1.1.0'>
</a>
<a href="https://travis-ci.org/MediaCluster/sbt-izpack/">
  <img src="https://travis-ci.org/MediaCluster/sbt-izpack.png" alt="Travis Build">
</a>

Allows `IzPack 4` to be used from within SBT. Builds on `sbt-native-packager` in order to produce an installer
package from staged distribution files.

To use this plugin use the addSbtPlugin command within your project's plugins.sbt (or as a global setting) i.e.:

```
addSbtPlugin("de.mediacluster.sbt" % "sbt-izpack" % "1.1.0")
```

Your project's build file also needs to enable this plugin. For example with build.sbt:

```
lazy val root = project.in(file(".")).enablePlugins(SbtIzpack)
```

The following options are available:

Option                 | Description
-----------------------|------------
artifactName*          | Function that produces the artifact name from its definition.
compressionFormat      | Defines the format for compressing packs.
compressionLevel       | The compression level, if supported by the compression format.
configFile             | Configures the path to the configuration xml file (default is `sourceDirectory`/install.xml).
kind                   | The packaging kind (default is "standard")
sourceDirectory*       | Directory for installer sources, passed as base path to the installer.
target*                | Target directory where the installer file is generated.

\* Denotes option keys which need to be bound to the `izpack` task.

Variables
---------

The following variables are available inside the XML configuration file:

Key               | Description
------------------|------------
appName           | Project name.
appVersion        | Project version with qualifier.
artifactPath      | Full path to the installer file.
baseDirectory     | Base directory of the project.
normalizedAppName | Normalized name of the project.
scalaVersion      | Scala version of the build.
stagingDirectory  | Path to the directory where the distribution files are staged.


Changing the name of the generated artifact
-------------------------------------------

This plugin reuses sbt task-key `artifact-name` and binds it to its own task-key `izpack`. By default, the generated
artifact name is `@{name}_@{scalaVersion}-@{version}-installer.jar`.

For example, to produce a name without scala version:

```
artifactName in izpack := {
  (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
    artifact.name + "-" + module.revision + "." + artifact.extension
}
```

Copyright &copy; 2014-2016, MediaCluster GmbH
