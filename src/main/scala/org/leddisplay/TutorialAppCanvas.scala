package org.leddisplay

import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.timers._

/**
  * open index.html
  */
@JSExport
object TutorialAppCanvas {
  val TIMEOUT = 50

  @JSExport
  def main(div: html.Div) : Unit = {
    val display = new LedDisplayCanvas(div, cellSize = 10, margin = 1, width = 120, height = 10, color = "#ff0000")

    def loop : (Double) => Unit = (time: Double) => {
      display.scrollLeft()
      display.show()

      setTimeout(TIMEOUT) {
        dom.window.requestAnimationFrame(loop)
      }
    }

    ImageFont.readFont(font => {
//    TextFont.readFont("src/web/font.json", font => {
        Array.range(0, 1).foreach(y => {
          display.print(y * 8 + 1, 0, "Pippo de pippis", font)
        })

        display.show()

        setTimeout(TIMEOUT) {
          loop.apply(0)
        }
      }
    )

  }

}