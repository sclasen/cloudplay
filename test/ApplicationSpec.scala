package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import controllers.Application
import services.LockService
import org.specs2.execute.Results
import play.api.libs.json.JsString

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification with Results{
  
  "Application" should {

    "render the index page" in {
      val app = new Application {
        def lockSvc: LockService = ???
      }
      (contentAsJson(app.index()(FakeRequest()))  \ "name").as[JsString].value mustEqual("aname")
    }
  }
}