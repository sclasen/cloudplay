package processes

import play.api.Play
import play.api.Play.current
import services.{LockType, Services}
import org.slf4j.LoggerFactory


object  MigrationProcess extends Services {

    val log = LoggerFactory.getLogger("MigrationProcess")

    def start(){
      lockSvc.withLock(1,LockType.)
      log.info("Migrating all the things")
      log.info("Migrating all the things")
      log.info("Migrating all the things")
      log.info("Migrating all the things")
      log.info("Migrating all the things")
      log.info("Migrating all the things")
      Play.stop()
      sys.exit(0)
    }
}
