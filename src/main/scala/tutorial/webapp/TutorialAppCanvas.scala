package tutorial.webapp

import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js.Dynamic.{global => g}
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.timers._

/**
  * open index.html
  */
@JSExport
object TutorialAppCanvas {

  @JSExport
  def main(canvas: html.Canvas) : Unit = {
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    val display = new LedDisplayCanvas(ctx, cellSize = 10, margin = 1, width = 120, height = 8)


    def loop : (Double) => Unit = (time: Double) => {
      display.scrollLeft()
      display.show()

      setTimeout(100) {
        dom.window.requestAnimationFrame(loop)
      }
    }

    Font.readFont("font.json", font => {
          Array.range(0, 1).foreach(y => {
            display.print(0, y * 8, "010101001011", font, "#00ff00")
          })

          display.show()

          setTimeout(100) {
            loop.apply(0)
          }
        }
    )

  }

}