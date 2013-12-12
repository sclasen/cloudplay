package processes

import org.slf4j.LoggerFactory
import play.api.Play
import play.api.Play.current
import play.api.db.DB
import services.{LockType, Services}
import com.heroku.play.api.libs.security.CredentialsService


object MigrationProcess extends Services {

  val log = LoggerFactory.getLogger("MigrationProcess")
  val theMigratonSecret = CredentialsService.decryptCredential("SECRET","SECRET_KEY","secret.key.mask")

  def start() {
    DB.withTransaction {
      implicit c =>
        lockSvc.withTransactionalLock(1, LockType.MIGRATION_LOCK) {
          log.info(s"Dont log secret stuff!!  ${theMigratonSecret}")
          log.info("Migrating all the things")
          log.info("Migrating all the things")
          log.info("Migrating all the things")
          log.info("Migrating all the things")
          log.info("Migrating all the things")
          log.info("Migrating all the things")
        }.getOrElse {
          log.error("Someone else had the migration lock!!!!")
        }
    }
    Play.stop()
    sys.exit(0)
  }
}
