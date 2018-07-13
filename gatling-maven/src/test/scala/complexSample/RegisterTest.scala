package complexSample
import io.gatling.core.Predef.atOnceUsers

class RegisterTest extends Scenario {

  setUp(scn("register").inject(atOnceUsers(1))).protocols(httpConf)

}
