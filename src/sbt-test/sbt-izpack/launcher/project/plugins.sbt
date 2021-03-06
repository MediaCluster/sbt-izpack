addSbtPlugin("de.mediacluster.sbt" % "sbt-izpack" % sys.props("project.version"))

addSbtPlugin("com.typesafe.sbt" %% "sbt-native-packager" % "1.1.5")

resolvers ++= Seq(
  Resolver.mavenLocal,
  "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
)
