package org.leddisplay

import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.timers._

/**
  * Created by enrico on 10/20/16.
  */
@JSExport
class LedDisplayConfig() {
  @JSExport var cellSize: Int = 6

  @JSExport var margin: Int = 2

  @JSExport var width: Int = 100

  @JSExport var height: Int = 10

  @JSExport var color: String = "#ff0000"

  @JSExport var timeout: Int = 50

}

@JSExport
class LedDisplayManager(div: html.Div, config: LedDisplayConfig = new LedDisplayConfig) {
  var scrolling = false
  var scrollingText = ""
  var scrollingTextOffset = 0

  val display = new LedDisplayCanvas(div, cellSize = config.cellSize, margin = config.margin, width = config.width,
    height = config.height, color = config.color)
  var font: Font = null

  Font.readFont(font => {
    this.font = font

    display.show()

    setTimeout(config.timeout) {
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
      setTimeout(config.timeout) {
        setText(text)
      }
    } else {
      scrollingText = ""
      scrollingTextOffset = 0
      display.print(1, 1, text, font)
    }
  }

  @JSExport
  def addText(text: String): Unit = {
    if (font == null) {
      setTimeout(config.timeout) {
        addText(text)
      }
    } else {
      scrollingText += text
    }
  }

  @JSExport
  def clear(): Unit = {
    scrollingText = ""
    scrollingTextOffset = 0
    display.clear()
  }

  private def loop : (Double) => Unit = (time: Double) => {
    if (scrolling) {
      display.scrollLeft()
      if (scrollingText.nonEmpty) {
        val charFont = font.get(scrollingText(0))
        for (y <- 0 until font.size) {
          display.set(y + 1, display.width -1, charFont.get(y, scrollingTextOffset))
        }
        scrollingTextOffset += 1
        if (scrollingTextOffset >= font.size) {
          scrollingTextOffset = 0
          scrollingText = scrollingText.substring(1)
        }
      }
    }
    display.show()

    setTimeout(config.timeout) {
      dom.window.requestAnimationFrame(loop)
    }
  }
}
