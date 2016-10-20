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
  def main(canvas: html.Canvas) : Unit = {
    val display = new LedDisplayCanvas(canvas, cellSize = 10, margin = 1, width = 120, height = 10, onColor = "#ff0000")

    def loop : (Double) => Unit = (time: Double) => {
      display.scrollLeft()
      display.show()

      setTimeout(TIMEOUT) {
        dom.window.requestAnimationFrame(loop)
      }
    }

    Font.readFont(font => {
//    TextFont.readFont("src/web/font.json", font => {
        Array.range(0, 1).foreach(y => {
          display.print(0, y * 8 + 1, "Pippo de pippis", font)
        })

        display.show()

        setTimeout(TIMEOUT) {
          loop.apply(0)
        }
      }
    )

  }

}