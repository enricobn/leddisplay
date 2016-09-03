package tutorial.webapp

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{ReactDOM, ReactElement}
import org.scalajs.dom
import dom.document

import scala.scalajs.js.JSApp

object TutorialApp extends JSApp {
  var display = new LedDisplay(10, 1, 80, 80)

  def main(): Unit = {
    Font.readFont("font.json", font => {
        Array.range(0, 10).foreach(y =>
          display.print(0, y * 8, "0101010", font, "00ff00")
        )
        ReactDOM.render(display.show(), document.body)
      }
    )
  }

  def text(text: String): ReactElement = {
      <.p(text).render
  }
}