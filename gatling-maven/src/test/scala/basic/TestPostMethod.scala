package basic
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class TestPostMethod extends Simulation{

  val feeder = Array(
    Map("foo" -> "foo1", "bar" -> "bar1"),
    Map("foo" -> "foo2", "bar" -> "bar2"),
    Map("foo" -> "foo3", "bar" -> "bar3")
  ).queue
  object randomStringGenerator {
    def randomString(length: Int) = scala.util.Random.alphanumeric.filter(_.isLetter).take(length).mkString
  }
  val randomIdFeeder = Iterator.continually(Map("id" -> (scala.util.Random.nextInt(100))))


  val body1 = Map("password" -> "123456", "phone" -> "17077122222")
  val sentHeaders = Map("Content-Type" -> "application/json")
  val httpConf = http
      .baseURL("http://192.168.400.171")
//      .headers(sentHeaders)
      .disableWarmUp
      .extraInfoExtractor(extraInfo => List(extraInfo.response,extraInfo.request,extraInfo.session))//记录日志在Simulation.log中
  val scn = scenario("login")
      .exec(http("login")
      .post("/login/submit")
        .formParamMap(body1)
        .check(jsonPath("$..data.token").optional.saveAs("myresponse"))
        .check(bodyString.transform(_.split("msg")(1)).saveAs("test1")))
      .pause(4 )
      .exec { session =>
        // displays the content of the session in the console (debugging only)
        println(session)
        println(session.get("test1"))
        val maybeId = session.get("myresponse").asOption[String]
        println(maybeId.getOrElse("COULD NOT FIND ID"))
        // return the original session
        session
      }
  setUp(
    scn.inject(atOnceUsers(1))
       ).protocols(httpConf)

}

