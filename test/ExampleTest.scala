import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
 import models.Examples._
class ExampleSpec extends PlaySpec {

  "Equilibrist" should {

    "fail when too many birds" in  {
      val eq = Equilibriste()
      eq.landRight(12) must be(None)
    }

  }

}
