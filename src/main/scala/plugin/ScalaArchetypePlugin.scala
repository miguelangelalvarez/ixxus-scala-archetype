package plugin

import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import com.typesafe.sbt.packager.NativePackagerKeys
import com.typesafe.sbt.packager.archetypes.JavaServerAppPackaging
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import com.typesafe.sbt.packager.docker._
import com.typesafe.sbt.packager.universal.UniversalDeployPlugin
import com.typesafe.sbt.{SbtNativePackager, SbtScalariform}
import sbt.Keys._
import sbt.plugins.IvyPlugin
import sbt.{Def, _}
import scalariform.formatter.preferences._

import scala.collection.immutable.Seq

object ScalaArchetypePlugin extends AutoPlugin with NativePackagerKeys {
  override def requires: Plugins = IvyPlugin &&
    SbtScalariform &&
    SbtNativePackager &&
    JavaServerAppPackaging &&
    UniversalDeployPlugin &&
    DockerPlugin

  override def trigger: PluginTrigger = allRequirements

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    scalaVersion := "2.12.4",
    fork in run := true,
    fork in Test := true,
    fork in testOnly := true,
    connectInput in run := true,
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    publishMavenStyle := true,
    dockerRepository := None,
    packageName in Docker := name.value,
    version in Docker := version.value,
    dockerCommands := Seq(
      Cmd("FROM", "openjdk:8-jdk-slim"),
      Cmd("MAINTAINER", "migalvcon@gmail.com"),
      Cmd("ADD", s"opt/docker /opt/${name.value}"),
      Cmd("WORKDIR", s"/opt/${name.value}"),
      ExecCmd("RUN", "mkdir", "-p", s"/var/log/${name.value}"),
      ExecCmd("ENTRYPOINT", s"/opt/${name.value}/bin/${name.value}")),
    scalacOptions ++= CustomSettings.compileSettings) ++ CustomSettings.scalariformPreferences
}

object CustomSettings {

  lazy val compileSettings: Seq[String] = Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:_",
    "-target:jvm-1.8",
    "-unchecked",
    "-Xfuture",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused-import",
    "-Ywarn-value-discard",
    "-Ywarn-unused")

  val scalariformPreferences: Seq[Setting[_]] =
    SbtScalariform.autoImport.scalariformSettings(true) ++
    Seq(ScalariformKeys.preferences := ScalariformKeys.preferences.value
      .setPreference(RewriteArrowSymbols, false)
      .setPreference(PreserveSpaceBeforeArguments, false)
      .setPreference(SpaceBeforeColon, false)
      .setPreference(SpaceInsideBrackets, false)
      .setPreference(SpaceInsideParentheses, false)
      .setPreference(SpacesWithinPatternBinders, true)
      .setPreference(SpacesAroundMultiImports, true)
      .setPreference(IndentWithTabs, false)
      .setPreference(IndentSpaces, 2)
      .setPreference(IndentPackageBlocks, true)
      .setPreference(DoubleIndentConstructorArguments, true)
      .setPreference(IndentLocalDefs, false)
      .setPreference(AlignParameters, true)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 40)
      .setPreference(DanglingCloseParenthesis, Preserve)
      .setPreference(CompactStringConcatenation, true)
      .setPreference(FormatXml, true)
      .setPreference(NewlineAtEndOfFile, true))
}
