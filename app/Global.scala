import core.CorsFilter
import play.api._
import play.api.mvc._

object Global extends WithFilters(CorsFilter) with GlobalSettings {

}
