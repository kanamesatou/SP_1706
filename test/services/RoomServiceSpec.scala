package services

import models.data.Room
import org.scalatestplus.play.PlaySpec

/**
  * Created by satou on 2017/10/21.
  */
class RoomServiceSpec extends PlaySpec {

  object RS extends RoomService with MixInRoomRepository {
    override val roomRepository: RoomRepository = new RoomRepositoryMock
  }

  "RoomService" should {
    val room1 = Room.Form("/test/url", "テストルーム", "password")
    val room2 = room1.copy(url = "/test/url/xxx")

    "Roomのエントリー" in {
      RS.entry(room1) mustBe None
      RS.entry(room2) mustBe None
      RS.entry(room1).getClass mustBe classOf[Some[String]]
    }

    "RoomのURL検索" in {
      RS.findByUrl("/test/url") mustBe Some(Room(0, "/test/url", "テストルーム", "password"))
      RS.findByUrl("/test/url/xxx") mustBe Some(Room(1, "/test/url/xxx", "テストルーム", "password"))
      RS.findByUrl("/test/url/xxx/yyy") mustBe None
    }

    "Roomのid検索" in {
      RS.findById(-1) mustBe None
      RS.findById(0) mustBe Some(Room(0, "/test/url", "テストルーム", "password"))
      RS.findById(1) mustBe Some(Room(1, "/test/url/xxx", "テストルーム", "password"))
      RS.findById(2) mustBe None
    }

  }

}
