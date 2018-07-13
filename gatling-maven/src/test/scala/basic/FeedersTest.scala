package basic
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._


class FeedersTest extends Simulation{
  val csvFeeders = csv("test.csv").circular
  val jsonFeeders = jsonFile("test.json").circular
  val jdbcFeeders = jdbcFeeder("jdbc:mysql://192.168.400.113:3306/hlc_online","root","root","select *  from user_info limit 15,5")
  val httpConf = http
    .baseURL("http://192.168.400.117")
    .disableWarmUp
    .extraInfoExtractor(extraInfo => List(extraInfo.response,extraInfo.request,extraInfo.session))//记录日志在Simulation.log中
  val scn = scenario("test feeder")
    .feed(csvFeeders)
    .exec(http("test feeder")
      .post("/login/submit")
        .formParamMap(Map("password" -> "${pwd}", "phone" -> "${phone}"))
//          .formParamMap(Map("password" -> "${ui_password}", "phone" -> "${ui_phone}"))
      .check(jsonPath("$").optional.saveAs("data")))
    .exec { session =>
    // displays the content of the session in the console (debugging only)
//    println(session)
    println(session.get("data").asOption[String].getOrElse("COULD NOT FIND ID"))
    // return the original session
    session
  }
  setUp(scn.inject(rampUsers(2) over(2))).protocols(httpConf)
}
