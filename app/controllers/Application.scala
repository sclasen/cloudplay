package controllers

import play.api.mvc._
import play.api.libs.json.Json

object Application extends Controller {
  
  def index = Action{
    Ok(Json.obj("name"->"aname","value"->"avalue"))
  }
  
}