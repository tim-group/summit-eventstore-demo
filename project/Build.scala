import com.typesafe.sbteclipse.core.EclipsePlugin.EclipseKeys
import sbt.Keys._
import sbt.Package.ManifestAttributes
import sbt._
import sbtassembly.AssemblyUtils
import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin.{MergeStrategy, assemblySettings}


object SummitFxDemoEventStoreBuild extends Build {

  val scalaVersionRegex = "(\\d+)\\.(\\d+).*".r
  val buildNumber = "0.0." + sys.env.getOrElse("BUILD_NUMBER", "0-SNAPSHOT")

  val defaults = Seq(
    version := buildNumber,
    organization := "com.timgroup",
    scalaVersion := "2.11.0",
    crossScalaVersions := Seq("2.10.3", "2.11.0"),
    parallelExecution in Global := false,
    EclipseKeys.withSource := true,
    EclipseKeys.projectTransformerFactories := Seq(SarosEclipse.sarosCruftTransformer),
    libraryDependencies ++= Seq(
      "joda-time" % "joda-time" % "2.2",
      "org.joda" % "joda-convert" % "1.2",
      "com.typesafe" % "config" % "1.2.1",
      "org.scalatest" %% "scalatest" % "2.1.3" % "test",
      "junit" % "junit" % "4.11" % "test"
    ),
    packageOptions <<= (version, scalaVersion) map { (v, sv) => Seq(ManifestAttributes(("Implementation-Version", v + "_" + sv), ("X-Java-Version", "7"))) }
  )

  val fatJarSettings = assemblySettings ++ Seq(
      test in assembly := {},
    mergeStrategy in assembly <<= (mergeStrategy in assembly) { (mergeStrategy) =>
      {
        case "logback.xml" => new MergeStrategy {
          def name: String = "local copy (not from JAR)"

          def apply(tempDir: File, path: String, files: Seq[File]): Either[String, Seq[(File, String)]] = {
            Right(files.find(file => !AssemblyUtils.sourceOfFileForMerge(tempDir, file)._4).map((_, path)).toSeq)
          }
        }
        case "META-INF/spring.tooling" => MergeStrategy.concat
        case x                         => mergeStrategy(x)
      }
    }
  )

  val root = Project(id="root", base=file("."))
    .settings(defaults : _*)
    .settings(fatJarSettings: _*)
    .settings(jarName in assembly := s"summit-fx-demo-eventstore-${buildNumber}.jar")
    .settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)
    .settings(libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "com.jsuereth" %% "scala-arm" % "1.4",
    "org.apache.httpcomponents" % "httpclient" % "4.2" % "test",
    "com.geteventstore" %% "eventstore-client" % "1.0.1",
    "com.googlecode.protobuf-java-format" % "protobuf-java-format" % "1.2",
    "org.mockito" % "mockito-core" % "1.9.0" % "test"
  ))
    .settings(libraryDependencies <<= (libraryDependencies, scalaVersion) { (ld, sv) =>
    sv match {
      case scalaVersionRegex(major, minor) if major.toInt > 2 || (major == "2" && minor.toInt >= 11) => Seq("org.scala-lang.modules" %% "scala-xml" % "1.0.2") ++ ld
      case _ => ld
    }
  })
}
