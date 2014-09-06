package core

import org.joda.time.DateTime
import org.junit.runner._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.WithApplication

import twitter4j._

@RunWith(classOf[JUnitRunner])
class KeywordTweetReaderSpec extends Specification with Mockito {

  val s1 = mockStatus(1, 20)
  val s2 = mockStatus(2, 15)
  val s3 = mockStatus(3, 10)
  val s4 = mockStatus(4, 5)

  def mockStatus(id: Int, createdAtMinusMinutes: Int): Status = {
    val status = mock[Status]
    status.getId returns id
    status.getCreatedAt returns DateTime.now().minusMinutes(createdAtMinusMinutes).toDate

    status
  }

  "KeywordTweetReader" should {

    "get all tweets from sncf" in new WithApplication {
      val mockReader = mock[TweetReader]
      mockReader.getTweets(anyString) returns List()

      val keywordTweetReader = new KeywordTweetReader(mockReader)
      keywordTweetReader.getAllTweets() must beEmpty

      there was one(mockReader).getTweets("sncf")
      there was one(mockReader).getTweets("#sncf")
    }

    "get all tweets from sncf since a given id" in new WithApplication {
      val mockReader = mock[TweetReader]
      mockReader.getTweetsSince(anyString, anyLong) returns List(s1, s2)

      val sinceId = 1L

      val keywordTweetReader = new KeywordTweetReader(mockReader)
      val foundTweets = keywordTweetReader.getTweetsSinceId(sinceId)
      foundTweets.size must equalTo(2)
      foundTweets(0).getId must equalTo(2)

      there was one(mockReader).getTweetsSince("sncf", sinceId)
      there was one(mockReader).getTweetsSince("#sncf", sinceId)
    }

    "get unique tweets sorted by created" in new WithApplication {
      val tweets = List(s1, s4)
      val tweetsWithHashtag = List(s2, s1, s3)
      val allTweets = tweets ++ tweetsWithHashtag

      val mockReader = mock[TweetReader]
      mockReader.getTweets(anyString) returns allTweets

      val keywordTweetReader = new KeywordTweetReader(mockReader)
      val foundTweets = keywordTweetReader.getTweets()

      foundTweets.size must equalTo(4)
      foundTweets(0) must equalTo(s4)
      foundTweets(1) must equalTo(s3)
      foundTweets(2) must equalTo(s2)
      foundTweets(3) must equalTo(s1)
    }

  }
}
