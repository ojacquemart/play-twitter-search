package core

import scala.concurrent.Future

import play.Logger
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import controllers.Default

object CorsFilter extends Filter {

  def isPreFlight(r: RequestHeader) =
    r.method.toLowerCase().equals("options") && r.headers.get("Access-Control-Request-Method").nonEmpty


  def apply(f: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {
    if (isPreFlight(rh)) {
      Logger.info("[cors] Request is preflight")
      
      Future.successful(
        Default.Ok.withHeaders(
          "Access-Control-Allow-Origin" -> rh.headers.get("Origin").getOrElse("*"),
          "Access-Control-Allow-Methods" -> rh.headers.get("Access-Control-Request-Method").getOrElse("*"),
          "Access-Control-Allow-Headers" -> rh.headers.get("Access-Control-Request-Headers").getOrElse(""),
          "Access-Control-Allow-Credentials" -> "true"
        )
      )
    } else {
      Logger.trace("[cors] Request is normal")

      val result = f(rh)
      result.map { r =>
        r.withHeaders(
          "Access-Control-Allow-Origin" -> rh.headers.get("Origin").getOrElse("*"),
          "Access-Control-Allow-Methods" -> rh.headers.get("Access-Control-Request-Method").getOrElse("*"),
          "Access-Control-Allow-Headers" -> rh.headers.get("Access-Control-Request-Headers").getOrElse(""),
          "Access-Control-Allow-Credentials" -> "true"
        )
      }
    }
  }
}