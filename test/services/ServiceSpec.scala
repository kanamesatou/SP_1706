package services

import models.data.{Room, User}
import org.scalatestplus.play.PlaySpec

/**
  * Created by satou on 2017/10/22.
  */
class ServiceSpec extends PlaySpec {

  private val room1Form = Room.Form("/test/url", "テストルーム", "password")
  private val room2Form = room1Form.copy(url = "/test/url/xxx")

  private val room1 = Room(0, "/test/url", "テストルーム", "password")
  private val room2 = room1.copy(id = 1, url = "/test/url/xxx")

  "RoomService" should {

    "Roomのエントリー" in {
      RoomService.entry(room1Form) mustBe Some(room1)
      RoomService.entry(room2Form) mustBe Some(room2)
      RoomService.entry(room1Form) mustBe None
    }

    "RoomのURL検索" in {
      RoomService.findByUrl("/test/url") mustBe Some(Room(0, "/test/url", "テストルーム", "password"))
      RoomService.findByUrl("/test/url/xxx") mustBe Some(Room(1, "/test/url/xxx", "テストルーム", "password"))
      RoomService.findByUrl("/test/url/xxx/yyy") mustBe None
    }

    "Roomのid検索" in {
      RoomService.findById(-1) mustBe None
      RoomService.findById(0) mustBe Some(Room(0, "/test/url", "テストルーム", "password"))
      RoomService.findById(1) mustBe Some(Room(1, "/test/url/xxx", "テストルーム", "password"))
      RoomService.findById(2) mustBe None
    }

  }

  "UserService" should {

    "Userのエントリー" in {
      UserService.entry("/test/url", User.Form("XXX")) mustBe Some(User(0, room1.id, "XXX"))
      UserService.entry("/test/url", User.Form("XXX")) mustBe None
      UserService.entry("/test/url/xxx", User.Form("XXX")) mustBe Some(User(1, room2.id, "XXX"))
      UserService.entry("/test/url/xxx", User.Form("YYY")) mustBe Some(User(2, room2.id, "YYY"))
      UserService.entry("/test/url/yyy", User.Form("ZZZ")) mustBe None
    }

    "Userのid検索" in {
      UserService.findById(-1) mustBe None
      UserService.findById(0) mustBe Some(User(0, room1.id, "XXX"))
      UserService.findById(1) mustBe Some(User(1, room2.id, "XXX"))
      UserService.findById(2) mustBe Some(User(2, room2.id, "YYY"))
      UserService.findById(3) mustBe None
    }

  }

}
