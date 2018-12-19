//
// Copyright (c) 2014-2016, MediaCluster GmbH.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

name := "custom-panel"

organization := "de.mediacluster"

version := "1.0"

scalaVersion in ThisBuild := "2.11.1"

lazy val panels = project.in(file("panels"))
  .enablePlugins(JvmPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "org.codehaus.izpack" % "izpack-core" % "5.1.1" % "provided",
      "org.codehaus.izpack" % "izpack-gui" % "5.1.1" % "provided",
      "org.codehaus.izpack" % "izpack-panel" % "5.1.1" % "provided",
      "org.codehaus.izpack" % "izpack-installer" % "5.1.1" % "provided",
      "org.codehaus.izpack" % "izpack-api" % "5.1.1" % "provided"
    )
  )

lazy val root = project.in(file("."))
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(SbtIzPack)
  .dependsOn(panels)
  .aggregate(panels)
  .settings(
    TaskKey[Unit]("check") := {
      import java.util.jar.JarFile
      val name = "custom/CustomHelloPanel.class"
      val outFile = IzPackKeys.outputFile.value
      if (null == new JarFile(outFile).getJarEntry(name)) {
        sys.error("Jar entry " + name + " not found in " + outFile)
      }
      ()
    }
  )

