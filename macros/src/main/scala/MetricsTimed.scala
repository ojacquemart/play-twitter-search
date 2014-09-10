package macros

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

class timed(name: String) extends StaticAnnotation {

  def macroTransform(annottees: Any*) = macro TimedMacro.impl
}

object TimedMacro {
  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {

    import c.universe._

    // Resolve metric name
    val metricName = c.prefix.tree match {
      case Apply(_, List(Literal(Constant(x)))) => x.toString
      case _ => c.abort(c.enclosingPosition, "Metric name not found!")
    }

    def getBody(body: Tree) = {
      // Extract apply method
      val bodyData = body match {
        case q"$expr($exprss)" => (expr, exprss)
      }
      val headerBody = bodyData._1
      val innerBody = bodyData._2

      // Return new method with duration computation
      q"""
        $headerBody {
          val timer = com.kenshoo.play.metrics.MetricsRegistry.default.timer($metricName)
          val t0 = System.nanoTime()

          val r = $innerBody

          val t1 = System.nanoTime()
          val duration = t1 - t0
          timer.update(duration, java.util.concurrent.TimeUnit.NANOSECONDS)

          r
        }
    """
    }

    val t = annottees(0).tree match {
      // Match def method and add timed computation to body
      case q"$mods def $tname[..$tparams](...$paramss): $tpt = $body" => {
        val timedMethod = getBody(body)
        q"$mods def $tname[..$tparams](...$paramss): $tpt = { $timedMethod }"
      }
    }

    c.Expr[Any](t)
  }

}