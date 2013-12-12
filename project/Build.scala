import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "cloudplay"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(jdbc, anorm,
    "com.codahale.metrics" % "metrics-core" % "3.0.1",
    "com.librato.metrics" % "metrics-librato" % "3.0.1.RC2",
    "org.scalaz" %% "scalaz-core" % "7.0.4",
    "org.postgresql" % "postgresql" % "9.3-1100-jdbc41",
    "com.typesafe.akka" %% "akka-agent" % "2.2.0",
    "com.sclasen" %% "play-extras" % "0.2.14-SNAPSHOT"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings{
    resolvers += "oss" at "https://oss.sonatype.org/content/repositories/snapshots"
  }.settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*).settings( initialCommands := initConsole)

  def initConsole =
    """
      |import com.heroku.play.api.libs.security.CredentialsService
      |import play.api.Play.current
      |def startApp = new play.core.StaticApplication(new java.io.File("."))
      |def stopApp = play.api.Play.stop()
      |lazy val client = new services.Services{}
      |import client._
    """.stripMargin



}
