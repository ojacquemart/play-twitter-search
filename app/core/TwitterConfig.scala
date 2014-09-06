package core

import scala.collection.JavaConverters._

import play.api.Play
import play.api.Play.current

object TwitterConfig {

  val limit = Play.application.configuration.getInt("twitter.limit").get
  val keywords = Play.application.configuration.getStringList("twitter.keywords").get.asScala.toList
  val matchText = Play.application.configuration.getString("twitter.matchText")
  val cacheTtlInSeconds = Play.application.configuration.getInt("twitter.cache.ttlInSeconds").get

}
