package complexSample
import io.gatling.core.Predef._
import io.gatling.http.Predef._


trait Scenario extends Simulation{
  val httpConf = http
    .baseURL("http://192.168.400.117")
    .disableWarmUp
    .extraInfoExtractor(extraInfo => List(extraInfo.response,extraInfo.request,extraInfo.session))//记录日志在Simulation.log中

  val headers = Map("Content-Type" -> "application/json")

  def getCaptcha()=
    http("get captcha")
      .post("/register/captcha")
      .check(jsonPath("$.data.captcha").optional.saveAs("captcha"))

  def getTicket()=
    http("get ticket")
      .post("/register/sms")
      .formParam("captcha", "1234")
      .formParam("captcha_ticket", "${captcha}")
      .formParam("phone", "18077786815")
      .check(jsonPath("$").optional.saveAs("sms_ticket"))

  def scn(name:String)={
    scenario(name)
      .exec(getCaptcha())
      .pause(3)
      .exec(getTicket())
      .exec( session =>{
        // displays the content of the session in the console (debugging only)
        println(session)
        println(session.get("captcha").asOption[String])
        val maybeId = session.get("sms_ticket").asOption[String]
        println(maybeId.getOrElse("COULD NOT FIND ID"))
        // return the original session
        session})
  }
}

class Register extends Scenario{
  setUp(scn("register").inject(atOnceUsers(1))).protocols(httpConf)
}