package core

import twitter4j.Status

import play.api.Play.current
import play.api.cache.Cache

import macros._

class KeywordTweetReader(reader: TweetReader) {

  import KeywordTweetReader.uniqueAndSortedByCreated

  def getTweets(): List[Status] = {
    uniqueAndSortedByCreated(getAllTweets())
  }

  def getTweetsSinceId(sinceId: Long): List[Status] = {
    val tweets = getAllTweetsSince(sinceId)
    tweets.filterNot(s => s.getId == sinceId)
  }

  def getAllTweets(): List[Status] =
    TwitterConfig.keywords.flatMap(reader.getTweets(_))

  def getAllTweetsSince(sinceId: Long): List[Status] =
    TwitterConfig.keywords
      .flatMap(reader.getTweetsSince(_, sinceId))
      .filterNot(s => s.getId == sinceId)

}

object KeywordTweetReader {

  val keyCache = "tweets"

  val reader = new KeywordTweetReader(TweetReader.reader)


  def getCachedTweets() = {
    Cache.getOrElse[List[Status]](keyCache, TwitterConfig.cacheTtlInSeconds) {
       reader.getTweets()
    }
  }

  def getCachedTweetsSince(sinceId: Long) = {
      reader.getTweetsSinceId(sinceId)
  }

  def uniqueAndSortedByCreated(tweets: List[Status]): List[Status] = {
    tweets
      .toSet
      .toList
      .sortWith((a, b) => a.getCreatedAt.compareTo(b.getCreatedAt) == 1)
  }

}
