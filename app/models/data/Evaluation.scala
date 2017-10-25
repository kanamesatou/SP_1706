package models.data

import play.api.data
import play.api.data.Forms._
import play.api.data.format.Formats._
import util.Extension.FormExtension

/**
  * Created by satou on 2017/10/21.
  */
case class Evaluation(id: Long, roomId: Long, no: Int, userId: Long, evaluationType: Evaluation.Type)

object Evaluation {

  sealed trait Type

  case class Form(roomId: Long, no: Int, userId: Long, evaluationType: String)

  object Form extends FormExtension[Form] {

    val roomId = "roomId"
    val no = "no"
    val userId = "userId"
    val evaluationType = "evaluationType"

    val form = data.Form(
      mapping(
        roomId -> of[Long],
        no -> number,
        userId -> of[Long],
        evaluationType -> text
      )(Form.apply)(Form.unapply)
    )
  }


  object Type {

    def all: Seq[Type] = Seq(Good)

    def fromString(seed: String): Option[Type] = all.find(_.toString == seed)

  }

  case object Good extends Type
}

