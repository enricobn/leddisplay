package tutorial.webapp

import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.timers._

/**
  * Created by enrico on 10/20/16.
  */
@JSExport
class LedDisplayManager(canvas: html.Canvas, timeout: Int = 50) {
  var scrolling = false

  val display = new LedDisplayCanvas(canvas, cellSize = 10, margin = 1, width = 120, height = 10, onColor = "#ff0000")
  var font: Font = null

  Font.readFont(font => {
    this.font = font

    display.show()

    setTimeout(timeout) {
      loop.apply(0)
    }
  })

  @JSExport
  def setScrolling(scrolling: Boolean): Unit = {
    this.scrolling = scrolling
  }

  @JSExport
  def setText(text: String): Unit = {
    if (font == null) {
      setTimeout(timeout) {
        setText(text)
      }
    } else {
      display.print(0, 1, text, font)
    }
  }

  private def loop : (Double) => Unit = (time: Double) => {
    if (scrolling) {
      display.scrollLeft()
    }
    display.show()

    setTimeout(timeout) {
      dom.window.requestAnimationFrame(loop)
    }
  }
}
