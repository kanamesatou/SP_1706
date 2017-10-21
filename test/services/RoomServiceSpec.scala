package services

import models.data.Room
import org.scalatestplus.play.PlaySpec

/**
  * Created by satou on 2017/10/21.
  */
class RoomServiceSpec extends PlaySpec {

  object RoomService extends RoomService with MixInRoomRepository {
    override val roomRepository: RoomRepository = RoomRepositoryMock
  }

  "RoomService" should {
    val room1 = Room(0, "/test/url", "テストルーム", "password")
    val room2 = room1.copy(url = "/test/url/xxx")

    "Roomのエントリー" in {
      RoomService.entry(room1) mustBe None
      RoomService.entry(room2) mustBe None
      RoomService.entry(room1).getClass mustBe classOf[Some[String]]
    }

    "Roomの検索" in {
      RoomService.findByUrl("/test/url") mustBe Some(room1)
      RoomService.findByUrl("/test/url/xxx") mustBe Some(room2)
      RoomService.findByUrl("/test/url/xxx/yyy") mustBe None
    }

  }

}
