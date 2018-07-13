package basic
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._


class Register extends Simulation{
  val header = Map("Content-Type" -> "application/json")

  val httpConf = http
    .baseURL("http://192.168.400.117")
//    .headers(header)
    .disableWarmUp
    .extraInfoExtractor(extraInfo => List(extraInfo.response,extraInfo.request,extraInfo.session))//记录日志在Simulation.log中
  val scn = scenario("get captcha")
//   图形获取验证码

    .exec(http("get captcha")
      .post("/register/captcha")
      .check(jsonPath("$.data.captcha").optional.saveAs("captcha")))
    .pause(4)
//    .rendezVous(100)  // 集合点
//  获取短信验证码
    .exec(http("get captcha_ticket")
      .post("/register/sms")
      .formParam("captcha", "1234")
      .formParam("captcha_ticket", session=> session.get("captcha").asOption[String])
//      .formParam("captcha_ticket", "${captcha}")
      .formParam("phone", "18077786815")
      .check(jsonPath("$").optional.saveAs("sms_ticket")))
    .exec { session =>
      // displays the content of the session in the console (debugging only)
      println(session)
      println(session.get("captcha").asOption[String])
      val maybeId = session.get("sms_ticket").asOption[String]
      println(maybeId.getOrElse("COULD NOT FIND ID"))
      // return the original session
      session
    }
  setUp(
    scn.inject(atOnceUsers(1))).protocols(httpConf)
//    .assertions(
//    global.responseTime.max.lt(50),
//    global.successfulRequests.percent.gt(95)
//  )
//  before {
//    println("Simulation is about to start!")
//    import scala.util.Random
//    val feeder = Iterator.continually(Map("email" -> (Random.alphanumeric.take(20).mkString + "@foo.com")))
//    println(feeder)
//  }
//  after {
//    println("Simulation is finished!")
//  }
}
