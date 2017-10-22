package controllers

import models.data.User
import play.api.mvc._
import services.RoomService
import util.Extension.OptionExtension

/**
  * Created by satou on 2017/10/21.
  */
class ChatController extends Controller {
  implicit private val self = this

  def chatRoom(url: String) = Action { implicit request =>
    User.auth(url) { user =>
      RoomService.findById(user.roomId).map { room =>
        Ok(views.html.chat.room(room))
      }.orInternalServerError
    }
  }

}
