package models.data

/**
  * Created by satou on 2017/10/21.
  */
case class Chat(id: Long, roomId: Long, no: Int, userId: Long,
                timeStamp: String, replyTo: Option[Long], content: String)