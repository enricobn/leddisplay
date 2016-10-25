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
  @JSExport var cellSize: Int = 4

  @JSExport var margin: Int = 2

  @JSExport var width: Int = 100

  @JSExport var height: Int = 14

  @JSExport var color: String = "#ff0000"

  @JSExport var timeout: Int = 50

  @JSExport var fontFamily: String = "Courier New"

  @JSExport var fontSize: Int = 12

  @JSExport var fontColorThreshold: Int = 50

}

@JSExport
class LedDisplayManager(divId: String, config: LedDisplayConfig = new LedDisplayConfig) {
  private var scrollingText = ""
  private var scrollingTextOffset = 0
  private var scrollEmpty = false
  private var font: Font = null
  private val div = dom.document.getElementById(divId).asInstanceOf[html.Div]
  private val display = new LedDisplayCanvas(div, cellSize = config.cellSize, margin = config.margin, width = config.width,
    height = config.height, color = config.color)
  private val imageFont = false

  @JSExport
  var scrolling = false

  // only for test
  if (imageFont) {
    ImageFont.readFont(font => {
      this.font = font

      display.show()

      setTimeout(config.timeout) {
        loop.apply(0)
      }
    })
  } else {
    font = OffscreenFont.read(config.fontFamily, config.fontSize, config.fontColorThreshold)

    display.show()

    setTimeout(config.timeout) {
      loop.apply(0)
    }
  }

  @JSExport
  def setText(text: String): Unit = {
    if (font == null) {
      // waiting for the font to be loaded
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
        if (scrollEmpty) {
          scrollEmpty = false
        } else {
          val charFont = font.get(scrollingText(0))
          for (y <- 0 until font.size) {
            display.set(y + 1, display.width - 1, charFont.get(y, scrollingTextOffset))
          }
          scrollingTextOffset += 1
          if (scrollingTextOffset >= charFont.width) {
            scrollingTextOffset = 0
            scrollingText = scrollingText.substring(1)
            scrollEmpty = true
          }
        }
      }
    }
    display.show()

    setTimeout(config.timeout) {
      dom.window.requestAnimationFrame(loop)
    }
  }
}
