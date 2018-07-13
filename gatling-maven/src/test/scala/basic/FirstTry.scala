package basic

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class FirstTry extends Simulation{
  val httpConf = http
    .baseURL("http://192.168.400.101")
  val scn = scenario("first scn")
    .exec(http("first scn")
    .get("/register/captcha"))
    .pause(4)
  val new1 = scenario("first new")
    .exec(http("first new")
      .get("/register/captcha"))
    .pause(4)
  val test1 = scenario("first test")
    .exec(http("first test")
      .get("/register/captcha"))
    .pause(4)
  setUp(
//    scn.inject(atOnceUsers(50)).protocols(httpConf), //直接达到的用户数20
//    new1.inject(nothingFor(10), rampUsers(50) over ( 2 )).protocols(httpConf), //2秒内达到的用户数为50，开始前等待10s
//    test1.inject(rampUsers(50) over (2 )).protocols(httpConf)  //2秒内达到的用户数为50
//    scn.inject(constantUsersPerSec(20) during (15)).protocols(httpConf) // 每秒增加20个，持续15s
//    scn.inject(constantUsersPerSec(20) during (15) randomized).protocols(httpConf) //每秒增加20左右的随机个数，持续15s
//    scn.inject(rampUsersPerSec(10) to 20 during (100 )).protocols(httpConf)//在100s中将启动速率从每秒10加到20个（用户有规律的增加）
//    scn.inject(rampUsersPerSec(10) to 20 during (50 ) randomized).protocols(httpConf)//在50s中将启动速率从每秒10加到20个（用户随机的增加）
//    scn.inject(splitUsers(200) into (rampUsers(10) over (10)) separatedBy (5)).protocols(httpConf) // 循环执行1. 10秒内达到的用户数为10，2.暂停5s，直到达到200个用户（1、2、1、2...）
//    scn.inject(splitUsers(100) into (rampUsers(10) over (10)) separatedBy atOnceUsers(30)).protocols(httpConf) //循环执行1. 10秒内达到的用户数为10，2.突增30个用户，直到达到200个用户（1、2、1、2...）
//    scn.inject(heavisideUsers(100) over (20 )).protocols(httpConf)//海维赛德式增长（类似于抛物线样式），20s增加到100个用户
      scn.inject(rampUsers(5) over(5)) ).protocols(httpConf).throttle(
    //          reachRps(10) in (10),
                holdFor(2 ),
    //          jumpToRps(5),
    //          holdFor(5)

    )


}
