package util

import play.api.data.Form
import play.api.mvc.{Controller, Request, Result}

/**
  * Created by satou on 2017/10/21.
  */
object Extension {
  trait FormExtension[A] {
    val form: Form[A]
    def opt(implicit request: Request[_]): Option[A] = form.bindFromRequest().fold(_ => None, Some.apply)
  }

  implicit class OptionExtension(opt: Option[Result]) {
    def orBadRequest(implicit controller: Controller): Result =
      opt.getOrElse(controller.BadRequest("parameter problem."))
  }

}
