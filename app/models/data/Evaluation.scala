package models.data

/**
  * Created by satou on 2017/10/21.
  */
case class Evaluation(id: Long, chatId: Long, userId: Long, evaluationType: Evaluation.Type)

object Evaluation {

  sealed trait Type

  object Type {

    def all: Seq[Type] = Seq(Good)

    def fromString(seed: String): Option[Type] = all.find(_.toString == seed)

  }

  case object Good extends Type
}

