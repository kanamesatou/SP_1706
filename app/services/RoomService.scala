package services

import models.data.Room
import scala.collection.mutable.ListBuffer


/**
  * Created by satou on 2017/10/21.
  */
trait RoomRepository {

  /**
    * Roomを登録する
    * URLの重複があった場合はエラーメッセージを返す
    */
  def entry(room: Room.Form): Option[String]

  /**
    * URLに対応するRoomを返す
    */
  def findByUrl(url: String): Option[Room]

  /**
    * idに対応するRoomを返す
    */
  def findById(id: Long): Option[Room]

}

trait UsesRoomRepository {
  val roomRepository: RoomRepository
}

/**
  * DBを使ったRoomRepositoryの実装
  */
object RoomRepositoryImpl extends RoomRepository {

  def entry(room: Room.Form): Option[String] = ???

  def findByUrl(url: String): Option[Room] = ???

  def findById(id: Long): Option[Room] = ???

}

/**
  * DBを使わないRoomRepositoryの実装
  */
object RoomRepositoryMock extends RoomRepository {
  private val repo = ListBuffer.empty[Room]

  def entry(roomForm: Room.Form): Option[String] = repo.synchronized {
    findByUrl(roomForm.url).map(_ => s"このURLは既に登録されています").fold[Option[String]] {
      val room = Room(repo.length, roomForm.url, roomForm.name, roomForm.password)
      repo += room.copy(id = repo.length)
      None
    }(Some.apply)
  }

  def findByUrl(url: String): Option[Room] = repo.find(_.url == url)

  def findById(id: Long): Option[Room] = repo.find(_.id == id)
}

trait MixInRoomRepository {
  val roomRepository: RoomRepository = RoomRepositoryMock
}

trait RoomService extends UsesRoomRepository {

  /**
    * Roomをリポジトリに登録する
    * 登録成功時はNone, 失敗時はエラーメッセージを返す
    */
  def entry(roomForm: Room.Form): Option[String] = roomRepository.entry(roomForm)

  /**
    * URLに対応するRoomを返す
    * 存在する場合はRoom, 存在しない場合はNoneを返す
    */
  def findByUrl(url: String): Option[Room] = roomRepository.findByUrl(url)

  /**
    * idに対応するRoomを返す
    * 存在する場合はRoom, 存在しない場合はNoneを返す
    */
  def findById(id: Long): Option[Room] = roomRepository.findById(id)

}

object RoomService extends RoomService with MixInRoomRepository
