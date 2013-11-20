package processes

import akka.actor.{Props, ActorRef, PoisonPill, Actor}
import scala.util.Random
import scala.concurrent.duration._
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits.defaultContext


import akka.agent.Agent
import play.api.db.DB
import anorm._
import java.sql.Connection
import services.{LockType, LockService}
import org.slf4j.LoggerFactory

object ContinuousProcess{

  val log = LoggerFactory.getLogger("Continuous")
  val system = Akka.system
  val actors = Agent(Map.empty[Long, ActorRef])(system.dispatcher)


  def start(){
    log.info("START")
    (1 to 10000).foreach{
      id =>
        log.info(s"starting $id")
        actors.send{
          curr => curr + (id.toLong -> system.actorOf(Props(classOf[ContinuousActor], id.toLong)))
        }
    }
  }

  def process(id:Long){
    DB.withTransaction{
      implicit c:Connection =>
        LockService.withTransactionalLock(id, LockType.CONTINUOUS_LOCK){
          log.info(s"got lock for $id")
           SQL("select pg_sleep(1)").execute()
        }.getOrElse{
          log.warn(s"could not get CONTINUOUS LOCK FOR $id")
        }
    }
  }
}

case object RunProcessing
case object StopProcessing


class ContinuousActor(id:Long) extends Actor{
  import ContinuousProcess._

  override def preStart() {
    super.preStart()
    //randomly start processing over a 60 second window to not slam the db on restart
    val randomStartupDelay = (Random.nextInt(60) seconds)
    context.system.scheduler.scheduleOnce(randomStartupDelay, self, RunProcessing)
  }


  def receive = {
    case StopProcessing =>
      self ! PoisonPill
    case RunProcessing =>
      process(id)
      context.system.scheduler.scheduleOnce(60 seconds, self, RunProcessing)
  }
}