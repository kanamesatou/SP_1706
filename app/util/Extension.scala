package util

import play.api.data.Form
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json.{JsValue, Json, Writes}
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

    def orInternalServerError(implicit controller: Controller): Result =
      opt.getOrElse(controller.InternalServerError("sorry, server problem occurred."))
  }

  trait MapJsonWrite {
    protected implicit val mapInt2IntWrites: Writes[Map[Int, Int]] = new Writes[Map[Int, Int]] {
      def writes(map: Map[Int, Int]): JsValue =
        Json.obj(map.map{case (s, o) =>
          val ret: (String, JsValueWrapper) = s.toString -> o
          ret
        }.toSeq:_*)
    }

    protected implicit val mapInt2SeqStringWrites: Writes[Map[Int, Seq[String]]] = new Writes[Map[Int, Seq[String]]] {
      def writes(map: Map[Int, Seq[String]]): JsValue =
        Json.obj(map.map {
          case (key, value) =>
            val ret: (String, JsValueWrapper) = key.toString -> value
            ret
        }.toSeq:_*)
    }
  }


}
