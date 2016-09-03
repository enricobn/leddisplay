package tutorial.webapp

import japgolly.scalajs.react.ReactDOM
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.ext.Ajax
import upickle.default._

/**
  * Created by enrico on 9/3/16.
  */

object Font {
  type Font = List[CharFont]

  def readFont(url: String, onSuccess: (Font) => Unit) : Unit = {
    // for Ajax call
    import dom.ext._
    import scala.scalajs
    .concurrent
    .JSExecutionContext
    .Implicits
    .runNow

    Ajax.get(url).onSuccess{ case xhr =>
      val font = read[Font](xhr.responseText)
      onSuccess(font)
    }
  }
}

case class CharFont(char: String, map: List[String])

