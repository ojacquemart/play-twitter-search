package controllers

import core.{KeywordTweetReader, TweetReader}

import play.api.libs.json._
import play.api.mvc._

import TweetReader.statusWrites

object Application extends Controller {

  def tweets = Action {
    Ok(Json.toJson(KeywordTweetReader.getCachedTweets()))
  }

  def tweetsSince(sinceId: Long) = Action {
    Ok(Json.toJson(KeywordTweetReader.getCachedTweetsSince(sinceId)))
  }

}