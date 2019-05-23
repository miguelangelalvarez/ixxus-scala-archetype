import sbt.Keys._
import com.typesafe.sbt.SbtScalariform
import sbtsonar.SonarPlugin.autoImport.sonarProperties

addSbtPlugin("com.github.mwz" % "sbt-sonar" % "1.5.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.21")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.0.9")

sbtPlugin := true
name := "scala-archetype"
version := "1.0.0-SNAPSHOT"

publishMavenStyle := true

sonarProperties ++= Map(
  "sonar.host.url" -> "http://localhost:8080"
)

scapegoatVersion in ThisBuild := "1.3.4"

enablePlugins(SbtScalariform, JavaServerAppPackaging, UniversalDeployPlugin, DockerPlugin)
