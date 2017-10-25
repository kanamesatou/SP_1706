package util

import play.api.libs.json.{JsValue, Json, Writes}
import play.api.libs.json.Json.JsValueWrapper

/**
  * Created by satou on 2017/10/25.
  */
trait ImplicitValues {
  protected implicit val mapWrites: Writes[Map[Int, Int]] = new Writes[Map[Int, Int]] {
    def writes(map: Map[Int, Int]): JsValue =
      Json.obj(map.map{case (s, o) =>
        val ret: (String, JsValueWrapper) = s.toString -> o
        ret
      }.toSeq:_*)
  }
}
