package tutorial.webapp

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{ReactDOM, ReactElement}
import org.scalajs.dom.document

import scala.scalajs.js.JSApp


object TutorialApp extends JSApp {
  def main(): Unit = {
    ReactDOM.render(text("Hello world!"), document.body)
  }

  def text(text: String): ReactElement = {
      <.p(text).render
  }
}