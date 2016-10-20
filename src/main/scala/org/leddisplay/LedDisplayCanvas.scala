package org.leddisplay

import org.scalajs.dom
import org.scalajs.dom.ext.Color
import org.scalajs.dom.html

/**
  * Created by enrico on 9/3/16.
  */

object LedDisplayCanvas {
  val PI2 = 2 * Math.PI
}

class LedDisplayCanvas(val canvas: html.Canvas, val cellSize : Int, val margin : Int, val width : Int, val height : Int,
                       val onColor: String) {
  import LedDisplayCanvas._

  private val matrix = new Array[Array[Boolean]](height)
  private val screen = new Array[Array[Int]](height)
  private val halfCellSize: Int = cellSize / 2
  private val onColorColor = Color(onColor)
  private var changed = false
  private val colors = Array[String](
    deriveColor(onColor, 0.2),
    deriveColor(onColor, 0.4),
    deriveColor(onColor, 0.6),
    deriveColor(onColor, 0.8),
    onColorColor.toString()
  )

  canvas.width = width * (cellSize + margin)
  canvas.height = height * (cellSize + margin)

  val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  for (y <- 0 until height) {
    matrix(y) = new Array[Boolean](width)
    screen(y) = new Array[Int](width)
  }

  ctx.fillStyle = "black"
  ctx.fillRect(0, 0, width * (cellSize + margin) + margin, height * (cellSize + margin) + margin)

  clear()

//  val offscreenCanvas = dom.document.createElement("Canvas").asInstanceOf[html.Canvas]
//  offscreenCanvas.width = width * (cellSize + margin)
//  offscreenCanvas.height = height * (cellSize + margin)
//  val offScreenContext = offscreenCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
//  offScreenContext.fillStyle = "white"
//  offScreenContext.fillText("0", 0, 0)
//  val imageData = offScreenContext.getImageData(0, 0, 100, 100).data

  def show() {
    for ( y <- 0 until height )
      for ( x <- 0 until width ) {
        val oldValue = screen(y)(x)
        val newValue =
          if (matrix(y)(x)) {
            4
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
//    var image = offScreenContext.getImageData(0, 0, offscreenCanvas.width, offscreenCanvas.height).asInstanceOf[ImageData]
//    ctx.putImageData(image, 0, 0)
  }

  def clear() : Unit = {
    for ( y <- 0 until height )
      for ( x <- 0 until width )
        matrix(y)(x) = false

    changed = true
  }

  def set(x: Int, y: Int, active: Boolean) : Unit = {
    if (matrix(y)(x) != active) {
      changed = true
    }
    matrix(y)(x) = active
  }

  private def print(x: Int, y: Int, c: Char, font: Font) : Unit = {
    val charFont = font.get(c)

    print(x, y, charFont, font)
  }

  def print(x: Int, y: Int, s: String, font: Font) : Unit = {
    for (ix <- s.indices)
      print(x + ix * font.size, y, s.charAt(ix), font)

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

  private def print(x: Int, y: Int, charFont: CharFont, font: Font) : Unit = {
    for (iy <- 0 until font.size)
      for (ix <- 0 until font.size)
        if (charFont.get(iy, ix)) set(x + ix, y + iy, true)
  }

  private def square(ctx: dom.CanvasRenderingContext2D, x: Int, y: Int, color: Int) {
    ctx.fillStyle = colors(color)
//    ctx.fillRect(x, y, cellSize, cellSize)
    fillCircle(ctx, x, y)
  }

  def fillCircle(ctx: dom.CanvasRenderingContext2D, x: Int, y: Int): Unit = {
    ctx.beginPath()
    ctx.arc(x + halfCellSize +1, y + halfCellSize +1, halfCellSize, 0, PI2, false)
    ctx.fill()
    ctx.stroke()
  }

  def deriveColor(color: String, coeff: Double) : String = {
    deriveColor(Color(color), coeff)
  }

  def deriveColor(color: Color, coeff: Double) : String = {
    Color((color.r * coeff).toInt, (color.g * coeff).toInt, (color.b * coeff).toInt).toString()
  }
}
