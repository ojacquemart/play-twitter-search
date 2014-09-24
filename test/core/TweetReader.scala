package core

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.WithApplication

@RunWith(classOf[JUnitRunner])
class TweetReaderSpec extends Specification {

  "TweetReader" should {

   "apply the regex from the conf to a text" in new WithApplication {
     TweetReader.matches("Retard un jour, retard toujours") must beTrue
     TweetReader.matches("Encore un retard") must beTrue
     TweetReader.matches("Je suis en retard de 10m") must beTrue
     TweetReader.matches("#RETARD!!!") must beTrue
     TweetReader.matches("No problem") must beFalse
   }
  }

}
