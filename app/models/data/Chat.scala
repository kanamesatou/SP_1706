package models.data

import play.api.data.Forms._
import play.api.data
import play.api.data.format.Formats._
import play.api.libs.json.Json
import util.Extension.{AuthenticatableForm, FormExtension}

/**
  * Created by satou on 2017/10/21.
  */
case class Chat(id: Long, roomId: Long, no: Int, userId: Long,
                timeStamp: String, replyTo: Option[Long], content: String)

object Chat {

  implicit def jsonWrites = Json.writes[Chat]

  case class PostForm(roomId: Long, userId: Long, replyTo: Option[Long], content: String) extends AuthenticatableForm

  object PostForm extends FormExtension[PostForm] {
    val roomId = "roomId"
    val userId = "userId"
    val replyTo = "replyTo"
    val content = "content"

    val form = data.Form(
      mapping(
        roomId -> of[Long],
        userId -> of[Long],
        replyTo -> optional(of[Long]),
        content -> text
      )(PostForm.apply)(PostForm.unapply)
    )
  }

  case class AjaxForm(roomId: Long, userId: Long ,no: Int) extends AuthenticatableForm

  object AjaxForm extends FormExtension[AjaxForm] {
    val roomId = "roomId"
    val userId = "userId"
    val no = "no"

    val form = data.Form(
      mapping(
        roomId -> of[Long],
        userId -> of[Long],
        no -> number
      )(AjaxForm.apply)(AjaxForm.unapply)
    )
  }

}