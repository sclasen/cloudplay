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
    "org.postgresql" % "postgresql" % "9.3-1100-jdbc41"

  )

  val main = play.Project(appName, appVersion, appDependencies).settings()

}
