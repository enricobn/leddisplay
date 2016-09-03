package tutorial.webapp

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{ReactDOM, ReactElement}
import org.scalajs.dom
import dom.document

import scala.scalajs.js.JSApp

object TutorialApp extends JSApp {
  var display = new LedDisplay(10, 1, 80, 8)

  def main(): Unit = {
    Font.readFont("font.json", font => ReactDOM.render(<.div(font.head.char), document.body))

    /*
    display.set(20, 1, "ff0000")
    */

    //ReactDOM.render(display.show(), document.body)

  }

  def text(text: String): ReactElement = {
      <.p(text).render
  }
}