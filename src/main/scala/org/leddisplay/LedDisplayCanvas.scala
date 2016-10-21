package org.leddisplay

import org.scalajs.dom
import org.scalajs.dom.ext.Color
import org.scalajs.dom.{CanvasRenderingContext2D, html}
import org.scalajs.dom.html.Canvas

/**
  * Created by enrico on 9/3/16.
  */

object LedDisplayCanvas {
  val PI2 = 2 * Math.PI
}

class LedDisplayCanvas(val div: html.Div, val cellSize : Int, val margin : Int, val width : Int, val height : Int,
                       val color: String) {
  import LedDisplayCanvas._

  private val matrix = new Array[Array[Boolean]](height)
  private val screen = new Array[Array[Int]](height)
  private val halfCellSize: Int = cellSize / 2
  private val displayWidth: Int = width * (cellSize + margin)
  private val displayHeight: Int = height * (cellSize + margin)
  private val colors =
    Array[String](
      deriveColor(color, 0.20),
      deriveColor(color, 0.50),
      deriveColor(color, 0.75),
      color
    )
  private var changed = false

  for (y <- 0 until height) {
    matrix(y) = new Array[Boolean](width)
    screen(y) = new Array[Int](width)
  }

  clear()

  div.style.width = displayWidth + "px"
  div.style.height = displayHeight + "px"

  val ctx = createContext()

  addMask()

  show()

  def show() {
    for ( y <- 0 until height )
      for ( x <- 0 until width ) {
        val oldValue = screen(y)(x)
        val newValue =
          if (matrix(y)(x)) {
            3
          } else {
            Math.max(screen(y)(x) -1, 0)
          }
        if (oldValue != newValue) {
          changed = true
          screen(y)(x) = newValue
        }
      }

    if (!changed) return

    for ( y <- 0 until height )
      for ( x <- 0 until width )
        square(ctx, x * (cellSize + margin), y * (cellSize + margin), screen(y)(x))

    changed = false
  }

  def clear() : Unit = {
    for ( y <- 0 until height )
      for ( x <- 0 until width )
        matrix(y)(x) = false

    changed = true
  }

  def set(y: Int, x: Int, active: Boolean): Unit = {
    if (y >= matrix.length || x >= matrix(y).length) return

    if (matrix(y)(x) != active) {
      changed = true
    }
    matrix(y)(x) = active
  }

  def print(y: Int, x: Int, s: String, font: Font): Unit = {
    var xPixel = x
    for (ix <- s.indices) {
      val c = s.charAt(ix)
      val charFont = font.get(c)
      print(xPixel, y, c, charFont, font)
      xPixel += charFont.width + 1
    }

    changed = true
  }

  def scrollLeft() : Unit = {
    for ( y <- 0 until height )
      for ( x <- 0 until width) {
        val actualValue = matrix(y)(x)
        val newValue = if (x == width - 1) false else matrix(y)(x + 1)

        if (actualValue != newValue) {
          matrix(y)(x) = newValue
          changed = true
        }
      }
  }

  private def createContext() : CanvasRenderingContext2D = {
    val canvas = dom.document.createElement("Canvas").asInstanceOf[Canvas]
    canvas.style.zIndex = "0"
    canvas.style.position = "absolute"
    canvas.style.left = div.style.left// "0"
    canvas.style.top = div.style.top //"0"
    div.appendChild(canvas)

    canvas.width = displayWidth
    canvas.height = displayHeight

    canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
  }

  private def addMask(): Unit = {
    val maskCavas = dom.document.createElement("Canvas").asInstanceOf[Canvas]
    maskCavas.style.zIndex = "1"
    maskCavas.style.position = "absolute"
    maskCavas.style.left = div.style.left //"0"
    maskCavas.style.top = div.style.top //"0"
    div.appendChild(maskCavas)

    maskCavas.width = displayWidth
    maskCavas.height = displayHeight

    val maskContext = maskCavas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    maskContext.fillStyle = "black"
    maskContext.fillRect(0, 0, displayWidth + margin, displayHeight + margin)

    maskContext.globalCompositeOperation = "destination-out"
    for (y <- 0 until height)
      for (x <- 0 until width) {
        fillCircle(maskContext, x * (cellSize + margin), y * (cellSize + margin))
      }
    maskContext.globalCompositeOperation = "source-over"
  }

  private def print(x: Int, y: Int, c: Char, charFont: CharFont, font: Font) : Unit = {
    print(x, y, charFont, font)
  }

  private def print(x: Int, y: Int, charFont: CharFont, font: Font) : Unit = {
    for (iy <- 0 until font.size)
      for (ix <- 0 until charFont.width)
        if (charFont.get(iy, ix)) set(y + iy, x + ix, true)
  }

  private def square(ctx: CanvasRenderingContext2D, x: Int, y: Int, color: Int) {
    ctx.fillStyle = colors(color)
    ctx.fillRect(x, y, cellSize + margin, cellSize + margin)
  }

  private def fillCircle(ctx: CanvasRenderingContext2D, x: Int, y: Int): Unit = {
    ctx.beginPath()
    ctx.arc(x + halfCellSize + margin, y + halfCellSize + margin, halfCellSize, 0, PI2, false)
    ctx.fill()
    ctx.stroke()
  }

  private def deriveColor(color: String, coeff: Double) : String = {
    deriveColor(Color(color), coeff)
  }

  private def deriveColor(color: Color, coeff: Double) : String = {
    Color((color.r * coeff).toInt, (color.g * coeff).toInt, (color.b * coeff).toInt).toString()
  }
}
