package services

import java.util.Date

import models.data.Chat
import models.data.Chat.PostForm

import scala.collection.mutable.ListBuffer

/**
  * Created by satou on 2017/10/22.
  */
trait ChatRepository {
  def post(postForm: PostForm): Chat

  def all(roomId: Long): Seq[Chat]
}

object ChatRepositoryMock extends ChatRepository {
  private val repo = ListBuffer.empty[Chat]

  def post(postForm: PostForm): Chat = repo.synchronized {
    val chat = Chat(
      id = repo.length,
      roomId = postForm.roomId,
      no = repo.count(_.roomId == postForm.roomId) + 1,
      userId = postForm.userId,
      timeStamp = "%tY-%<tm-%<td %<tH:%<tM:%<tS" format new Date,
      replyTo = postForm.replyTo,
      content = postForm.content
    )
    repo += chat
    chat
  }

  def all(roomId: Long): Seq[Chat] = repo.filter(_.roomId == roomId).toList
}

trait UsesChatRepository {
  val chatRepository: ChatRepository
}

trait MixInChatRepository {
  val chatRepository: ChatRepository = ChatRepositoryMock
}


trait ChatService extends UsesChatRepository {

  /**
    * PostFormをもとにChatを登録する
    * Roomの存在、Userの存在、Userの所属Roomを調べ、
    * 正しい場合はChatを登録し、そのChatを返す
    * 正しくない場合は何もせずNoneを返す
    */
  def post(postForm: PostForm): Option[Chat] = {
    for {
      room <- RoomService.findById(postForm.roomId)
      user <- UserService.findById(postForm.userId)
      if room.id == user.roomId
    } yield chatRepository.post(postForm)
  }

  /**
    * Roomに投稿されたChatを全て返す
    */
  def all(roomId: Long): Seq[Chat] = chatRepository.all(roomId)
}

object ChatService extends ChatService with MixInChatRepository
