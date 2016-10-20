package org.leddisplay

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB, ReactDOM}
import org.scalajs.dom
import dom.document

import scala.scalajs.js.JSApp
import scala.scalajs.js

case class State(secondsElapsed: Long)

class Backend($: BackendScope[Unit, State]) {
  var interval: js.UndefOr[js.timers.SetIntervalHandle] =
    js.undefined

  def tick =
    $.modState(s => State(s.secondsElapsed + 1))

  def start = Callback {
    interval = js.timers.setInterval(500)(tick.runNow())
  }

  def clear = Callback {
    interval foreach js.timers.clearInterval
    interval = js.undefined
  }

  def render(s: State) = {
//    dom.console.log("Start rendering")
//    var time = System.currentTimeMillis()
    var result : ReactTag = null
    if (s.secondsElapsed <= 60) {
      TutorialAppReact.display.scrollLeft()
//      dom.console.log("scrollLeft " + (System.currentTimeMillis() - time))
//      time = System.currentTimeMillis()
      result = TutorialAppReact.display.show()
//      dom.console.log("show " + (System.currentTimeMillis() - time))
    } else {
      result = <.div("Seconds elapsed: ", s.secondsElapsed)
    }
    result
  }
}

/**
  * open http://localhost:63342/led_display/index.html
  */
object TutorialAppReact extends JSApp {
  val display = new LedDisplay(10, 1, 120, 24)

  def main(): Unit = {
    /*
    Font.readFont("font.json", font => {
        Array.range(0, 10).foreach(y =>
          display.print(0, y * 8, "0101010", font, "00ff00")
        )
        ReactDOM.render(display.show(), document.body)
        g.window.requestAnimationFrame(loop)
      }
    )*/

    val Timer = ReactComponentB[Unit]("Timer")
      .initialState(State(0))
      .renderBackend[Backend]
      .componentDidMount(_.backend.start)
      .componentWillUnmount(_.backend.clear)
      .build

    TextFont.readFont("src/web/font.json", font => {
      Array.range(0, 3).foreach(y =>
        display.print(0, y * 8, "0101010", font, "00ff00")
      )
    }
    )

    ReactDOM.render(Timer(), document.body)
  }

  /*
  private def loop(): Unit = {
    display.scrollLeft()
    ReactDOM.render(display.show(), document.body)
    //g.window.requestAnimationFrame(loop)
  }
*/
}
