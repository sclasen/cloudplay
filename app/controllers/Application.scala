package controllers

import play.api.mvc._
import play.api.libs.json.Json
import services.{Services, ServicesCake}

object Application extends Application with Services

trait Application extends Controller with ServicesCake {

  def index = Action{
    Ok(Json.obj("name"->"aname","value"->"avalue"))
  }

}