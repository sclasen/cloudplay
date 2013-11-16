import Metrics._
import com.codahale.metrics.{JmxAttributeGauge, MetricRegistry}
import com.librato.metrics.{LibratoReporter, Sanitizer}
import java.util.concurrent.TimeUnit
import javax.management.ObjectName
import org.slf4j.LoggerFactory
import play.api.Application
import play.api.GlobalSettings
import play.api.mvc._
import scala.concurrent.Future

object Metrics{
  lazy val metrics = new MetricRegistry
}


trait ProcessType extends GlobalSettings {

  val log = LoggerFactory.getLogger(this.getClass)

  val fiveHundredMeter = metrics.meter("uncaught.application.errors.500")

  def start(app: Application, proc:String) {
    setupBoneCPMetrics(app)
    startReporter(app,proc)
  }

  val boneCPAttributes = List("TotalLeased", "TotalFree", "TotalCreatedConnections", "ConnectionWaitTimeAvg", "StatementExecuteTimeAvg", "StatementPrepareTimeAvg", "StatementsPrepared",
    "CacheHits", "CacheMiss", "StatementsCached", "ConnectionsRequested", "CumulativeConnectionWaitTime", "CacheHitRatio", "StatementsExecuted", "CumulativeStatementExecutionTime", "CumulativeStatementPrepareTime")

  def setupBoneCPMetrics(app: Application) {
    if (!app.configuration.getBoolean("db.default.disableJMX").getOrElse(true)) {
      boneCPAttributes.foreach {
        a => metrics.register(s"play.connection-pool.$a", new JmxAttributeGauge(new ObjectName("com.jolbox.bonecp:type=BoneCP"), a))
      }
    } else log.info("BoneCP JMX Not Enabled")
  }

  def startReporter(app:Application,proc:String){
    val libratoUsername = app.configuration.getString("librato.username")
    val libratoToken = app.configuration.getString("librato.token")
    val libratoInterval = app.configuration.getInt("librato.interval").getOrElse(30).toLong
    val appName = app.configuration.getString("heroku.app")
    import scalaz.std.option._
    import scalaz.syntax.applicative._
    (libratoUsername |@| libratoToken) {
      (user, token) =>
      val appPrefix = appName.map(_.replace('-','.'))
      val addApp = new Sanitizer {
        //the sanitizer can be applied more than once, so we make sure to only add the prefix once
        def apply(name: String): String = appPrefix.filter(p => !name.startsWith(p)).map(app => app + "." + name).getOrElse(name)
      }
     LibratoReporter.enable(LibratoReporter.builder(metrics, user, token, appName.getOrElse("unknown") + "." + proc)
        .setTimeout(libratoInterval - 5, TimeUnit.SECONDS).setSanitizer(addApp), libratoInterval, TimeUnit.SECONDS)
    }.getOrElse(log.error("librato username or token missing, not enabling librato"))
  }

  override def onError(request: RequestHeader, ex: Throwable): Future[SimpleResult] = {
    fiveHundredMeter.mark()
    super.onError(request, ex)
  }

}


object Web extends ProcessType  {
  override def onStart(app: Application) = {
    log.info("Starting ProcessType: Web")
    start(app, "web")
  }
}

object Continuous extends ProcessType {
  override def onStart(app: Application) = {
    log.info("Starting ProcessType: Continuous")
    start(app, "continuous")
    processes.ContinuousProcess.start()
  }
}
