package controllers

import core.{KeywordTweetReader, TweetReader}
import macros.timed

import play.api.libs.json._
import play.api.mvc._

import TweetReader.statusWrites

object Application extends Controller {

  @timed("app.tweets")
  def tweets = Action {
    Ok(Json.toJson(KeywordTweetReader.getCachedTweets()))
  }

  @timed("app.tweets.since")
  def tweetsSince(sinceId: Long) = Action {
    Ok(Json.toJson(KeywordTweetReader.getCachedTweetsSince(sinceId)))
  }

}