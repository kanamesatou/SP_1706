package controllers

import models.data.Chat.{AjaxForm, PostForm}
import models.data.{Chat, Evaluation, User}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.libs.json.Json
import play.api.mvc._
import services._
import util.Extension.OptionExtension
import util.ImplicitValues

/**
  * Created by satou on 2017/10/21.
  */
class ChatController extends Controller with ImplicitValues {
  implicit private val self = this

  def chatRoom(url: String) = Action { implicit request =>
    RoomService.findByUrl(url).map { room =>
      User.authenticate(room.id) { user =>
        Ok(views.html.chat.room(room, user))
      }(Ok(views.html.login(room.url)))
    }.getOrElse(BadRequest)
  }

  def postApi = Action { implicit request =>
    (for {
      postForm <- PostForm.opt
      room <- RoomService.findById(postForm.roomId)
      user <- UserService.findById(postForm.userId)
      if user.roomId == room.id
      chat <- ChatService.post(postForm)
    } yield {
      Ok(Json.toJson(chat))
    }).orBadRequest
  }

  def getApi = Action { implicit request =>
    Chat.AjaxForm.opt.map { ajaxForm =>
      User.authenticate(ajaxForm.roomId) { _ =>
        Ok(Json.toJson(ChatService.versioned(ajaxForm.roomId, ajaxForm.no)))
      }(Forbidden)
    }.orBadRequest
  }

  def evaluationApi = Action { implicit request =>
    (for {
      evaluationForm <- Evaluation.Form.opt
      room <- RoomService.findById(evaluationForm.roomId)
      user <- UserService.findById(evaluationForm.userId)
      if room.id == user.roomId
      _ <- EvaluationService.entry(evaluationForm)
    } yield {
      Ok
    }).orBadRequest
  }

  def getEvaluationApi = Action { implicit request =>
    Form(AjaxForm.roomId -> of[Long]).bindFromRequest.fold(
      _ => BadRequest
      ,
      roomId => {
        User.authenticate(roomId) { _ =>
          Ok(Json.toJson(EvaluationService.resultMap(roomId)))
        }(Forbidden)
      }
    )
  }

}
