package core

import scala.collection.JavaConverters._
import twitter4j.{Twitter, Query, Status, TwitterFactory}

import play.api.Logger
import play.api.libs.json.{Json, Writes}

class TweetReader(twitter: Twitter) {

  def getTweets(keyword: String): List[Status] = {
    val since: Query = QueryHelper.get(keyword)

    search(since)
  }

  def getTweetsSince(keyword: String, sinceId: Long): List[Status] = {
    val since: Query = QueryHelper.getWithSince(keyword, sinceId)

    search(since)
  }

  def search(query: Query): List[Status] = {
    val start = System.currentTimeMillis()
    Logger.info(s"Starting to get tweets for '${query.getQuery}' since '${query.getSince}'")
    val tweets = twitter.search(query).getTweets
      .asScala
      .toList
      .filterNot(_.isRetweet)

    val duration = System.currentTimeMillis() - start

    Logger.info(s"Get ${tweets.size} tweets for '${query.getQuery}' since '${query.getMaxId}' in ${duration} ms")

    tweets
  }

}

object QueryHelper {

  def get(keyword: String) = {
    val query = new Query(keyword)
    query.count(TwitterConfig.limit)

    query
  }

  def getWithSince(keyword: String, sinceId: Long) = {
    val query = get(keyword)
    query.setMaxId(sinceId)

    query
  }

}

object TweetReader {

  val twitterBaseUrl = "https://twitter.com"
  val canMatchText = TwitterConfig.matchText.isDefined
  val matchText = if (canMatchText) TwitterConfig.matchText.get else ""

  val twitterSingleton = TwitterFactory.getSingleton
  val reader = new TweetReader(twitterSingleton)

  def getTweets(keyword: String) = reader.getTweets(keyword)

  def matches(text: String) = {
    if (!canMatchText) false
    else text.matches(matchText)
  }

  implicit val statusWrites = new Writes[Status] {
    def writes(status: Status) = {
      val user = status.getUser

      Json.obj(
        "id" -> status.getId,
        "createdAt" -> status.getCreatedAt,
        "url" -> s"${twitterBaseUrl}/${user.getScreenName}/status/${status.getId}",
        "text" -> status.getText,
        "favoriteCount" -> status.getFavoriteCount,
        "retweetCount" -> status.getRetweetCount,
        "source" -> status.getSource,
        "matches" -> matches(status.getText),
        "user" ->  Json.obj(
          "id" -> user.getId,
          "name" -> user.getName,
          "screenName" -> user.getScreenName,
          "profileImageURL" -> user.getProfileImageURL,
          "profileURL" -> s"${twitterBaseUrl}/${user.getScreenName}"
        )
      )
    }
  }
}