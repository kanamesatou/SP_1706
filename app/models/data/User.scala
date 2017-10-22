package models.data

import play.api.data
import play.api.data.Forms._
import play.api.mvc.{Controller, Request, Result}
import services.{RoomService, UserService}
import util.Extension.FormExtension

import scala.util.Try

/**
  * Created by satou on 2017/10/21.
  */
case class User(id: Long, roomId: Long, nickName: String)

object User {
  def sessionName(url: String): String = s"pseudcussion[$url]"

  def authenticate[A](url: String)(ifSuccess: User => A)(ifFailed: => A)(implicit request: Request[_]): A =
    request.session
      .get(sessionName(url))
      .flatMap(id => Try(id.toLong).toOption)
      .flatMap(UserService.findById)
      .fold(ifFailed)(ifSuccess)

  def authenticate[A](roomId: Long)(ifSuccess: User => A)(ifFailed: => A)(implicit request: Request[_]): A =
    RoomService.findById(roomId).fold(ifFailed)(r => authenticate(r.url)(ifSuccess)(ifFailed))

  def authenticate[A](ifSuccess: User => A)(ifFailed: => A)(implicit request: Request[_]): A =
    request.session
      .data
        .find { case (key, _) => request.uri.endsWith(key) }
        .map(_._1)
        .fold(ifFailed)(url => authenticate(url)(ifSuccess)(ifFailed))


  def auth(url: String)(ifSuccess: User => Result)(implicit request: Request[_], controller: Controller): Result =
    authenticate(url)(ifSuccess)(controller.Ok(views.html.login(url)))


  case class Form(nickName: String)

  object Form extends FormExtension[Form] {
    val nickName = "nickName"

    val form = data.Form(
      mapping(
        nickName -> text
      )(Form.apply)(Form.unapply)
    )
  }

}
