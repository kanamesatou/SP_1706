package controllers

import models.data.Room
import play.api.mvc._
import services.RoomService
import util.Extension.OptionExtension

/**
  * Created by satou on 2017/10/21.
  */
class RoomController extends Controller {
  implicit private val self = this

  def entryPage = Action {
    Ok(views.html.room.entry())
  }

  def entry = Action { implicit request =>
    Room.Form.opt.map { form =>
      RoomService.entry(form)
      Ok(views.html.room.entry_complete(form.url))
    }.orBadRequest
  }



}
