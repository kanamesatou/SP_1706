package services

import models.data.{Room, User}
import org.scalatestplus.play.PlaySpec

/**
  * Created by satou on 2017/10/22.
  */
class UserServiceSpec extends PlaySpec {

  object US extends UserService with MixInUserRepository {
    override val userRepository = UserRepositoryMock
  }

  object RS extends RoomService with MixInRoomRepository {
    override val roomRepository: RoomRepository = new RoomRepositoryMock
  }

  RS.entry(Room.Form("/test", "テストルーム1", "password"))
  RS.entry(Room.Form("/test/xxx", "テストルーム2", "possward"))

  "UserService" should {

    "Userのエントリー" in {
      US.entry("/test", User.Form("XXX")) mustBe Some(User(0, 0, "XXX"))
      US.entry("/test", User.Form("XXX")) mustBe None
      US.entry("/test/xxx", User.Form("XXX")) mustBe Some(User(1, 1, "XXX"))
      US.entry("/test/xxx", User.Form("YYY")) mustBe Some(User(2, 1, "YYY"))
      US.entry("/test/yyy", User.Form("ZZZ")) mustBe None
    }

    "Userのid検索" in {
      US.findById(-1) mustBe None
      US.findById(0) mustBe Some(User(0, 0, "XXX"))
      US.findById(1) mustBe Some(User(1, 1, "XXX"))
      US.findById(2) mustBe Some(User(2, 1, "YYY"))
      US.findById(3) mustBe None
    }

  }

}
