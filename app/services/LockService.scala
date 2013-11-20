package services

import java.sql.Connection
import language.reflectiveCalls

import anorm._
import anorm.SqlParser._

//Say not caking

object LockService extends LockService

trait LockService {

  def withTransactionalLock[T](lockId: Long, lockType: LockType)(block: => T)(implicit c: Connection): Option[T] = {
    if (c.getAutoCommit) throw new RuntimeException("Cant use withTransactionalLock with a connection on autoCommit")
    if (transactionalLock(lockId, lockType)) {
      val t = block
      Some(t)
    } else None
  }

  def withLock[T](lockId: Long, lockType: LockType)(block: => T)(implicit c: Connection): Option[T] = {
    if (lock(lockId, lockType)) {
      try {
        val t = block
        Some(t)
      } finally {
        unlock(lockId, lockType)
      }

    } else None
  }

  def lock(lockId: Long, lockType: LockType)(implicit c: Connection): Boolean = {
    SQL("select pg_try_advisory_lock({id},{type}) as lock").on("id" -> lockIdToInt(lockId), "type" -> lockType.ordinal()).single(bool("lock"))
  }

  def unlock(lockId: Long, lockType: LockType)(implicit c: Connection): Boolean = {
    SQL("select pg_advisory_unlock({id},{type}) as lock").on("id" -> lockIdToInt(lockId), "type" -> lockType.ordinal()).single(bool("lock"))
  }

  def transactionalLock(lockId: Long, lockType: LockType)(implicit c: Connection): Boolean = {
    SQL("select pg_try_advisory_xact_lock({id},{type}) as lock").on("id" -> lockIdToInt(lockId), "type" -> lockType.ordinal()).single(bool("lock"))
  }

  private def lockIdToInt(id: Long): Int = {
    //if there happen to be two lockIds in your app that happen to have the same toInt value, they will share time with each other nbd usually.
    id.toInt
  }

}
