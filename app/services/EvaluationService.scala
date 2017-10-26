package services

import models.data.Evaluation

import scala.collection.mutable.ListBuffer
import scala.util.Try

/**
  * Created by satou on 2017/10/25.
  */

trait EvaluationRepository {

  def entry(form: Evaluation.Form): Option[Evaluation]

  def exists(form: Evaluation.Form): Boolean

  def all(roomId: Long): Seq[Evaluation]

}

object EvaluationRepositoryMock extends EvaluationRepository {
  private val repo = ListBuffer.empty[Evaluation]

  def entry(form: Evaluation.Form): Option[Evaluation] = repo.synchronized {
    Evaluation.Type.fromString(form.evaluationType).map { evaluationType =>
      val evaluation = Evaluation(
        id = repo.length,
        roomId = form.roomId,
        no = form.no,
        userId = form.userId,
        evaluationType = evaluationType
      )
      repo += evaluation
      evaluation
    }
  }

  def exists(form: Evaluation.Form): Boolean = repo.exists { eval =>
    eval.roomId == form.roomId &&
    eval.no == form.no &&
    eval.userId == form.userId
  }

  def all(roomId: Long): Seq[Evaluation] = repo.filter(_.roomId == roomId)

}

trait UsesEvaluationRepository {
  val evaluationRepository: EvaluationRepository
}

trait MixInEvaluationRepository {
  val evaluationRepository: EvaluationRepository = EvaluationRepositoryMock
}

trait EvaluationService extends UsesEvaluationRepository {

  /**
    * Evaluation.Formを登録する
    * Chat投稿者と同一人物や、2度目のEvaluationは登録せず、Noneを返す
    */
  def entry(form: Evaluation.Form): Option[Evaluation] = {
    Try { ChatService.all(form.roomId)(form.no - 1) }.toOption.flatMap { chat =>
      if ( chat.userId != form.userId && !evaluationRepository.exists(form)) evaluationRepository.entry(form)
      else None
    }
  }

  /**
    * Room内のすべてのEvaluationを取得する
    */
  def all(roomId: Long): Seq[Evaluation] = evaluationRepository.all(roomId)

  /**
    * Room内のすべてのChatに対し、いくつEvaluationがあるかをカウントする
    */
  def resultMap(roomId: Long): Map[Int, Int] = ChatService.all(roomId).map { chat =>
    chat.no -> all(roomId).count(_.no == chat.no)
  }.toMap
}

object EvaluationService extends EvaluationService with MixInEvaluationRepository
