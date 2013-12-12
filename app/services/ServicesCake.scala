package services


trait Services extends ServicesCake {
  def lockSvc: LockService = LockService
}

trait ServicesCake {
  def lockSvc:LockService
}
