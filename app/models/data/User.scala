package models.data

import play.api.data
import play.api.data.Forms._
import play.api.mvc.{Controller, Request, Result}
import services.UserService
import util.Extension.FormExtension

import scala.util.Try

/**
  * Created by satou on 2017/10/21.
  */
case class User(id: Long, roomId: Long, nickName: String)

object User {
  private def sessionName(url: String): String = s"pseudcussion[$url]"

  def authenticate[A](url: String)(ifSuccess: User => A)(ifFailed: => A)(implicit request: Request[_]): A =
    request.session
      .get(sessionName(url))
      .flatMap(id => Try(id.toLong).toOption)
      .flatMap(UserService.findById)
      .fold(ifFailed)(ifSuccess)

  def auth(url: String)(ifSuccess: User => Result)(implicit request: Request[_], controller: Controller): Result =
    authenticate(url)(ifSuccess)(controller.Ok("")) // TODO login page

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
