import com.kenshoo.play.metrics.MetricsFilter

import core.CorsFilter

import play.api._
import play.api.mvc._

object Global extends WithFilters(CorsFilter, MetricsFilter) with GlobalSettings {

}
