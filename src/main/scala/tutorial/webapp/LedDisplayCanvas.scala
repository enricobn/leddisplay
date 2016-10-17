package tutorial.webapp

import org.scalajs.dom
import org.scalajs.dom.ext.Color
import tutorial.webapp.Font.Font

import scala.collection.immutable.IndexedSeq

/**
  * Created by enrico on 9/3/16.
  */

object LedDisplayCanvas {
  val PI2 = 2 * Math.PI
}

class LedDisplayCanvas(ctx: dom.CanvasRenderingContext2D, cellSize : Int, margin : Int, width : Int, height : Int,
                       onColor: String) {
  import LedDisplayCanvas._

  private val matrix = new Array[Array[Double]](height)
  private val halfCellSize: Int = cellSize / 2
  private val offColorCoeff = 0.2
  private val offColor = deriveColor(onColor, offColorCoeff)
  private val onColorColor = Color(onColor)
  private var changed = false

  for (y <- 0 until height)
    matrix(y) = new Array[Double](width)

  clear()

//  val offscreenCanvas = dom.document.createElement("Canvas").asInstanceOf[html.Canvas]
//  offscreenCanvas.width = width * (cellSize + margin)
//  offscreenCanvas.height = height * (cellSize + margin)
//  val offScreenContext = offscreenCanvas.getContext("2d")

  def show() {
    if (!changed) return

    ctx.fillStyle = "black"
    ctx.fillRect(0, 0, width * (cellSize + margin) + margin, height * (cellSize + margin) + margin)

    for ( y <- 0 until height )
      for ( x <- 0 until width )
        square(ctx, x * (cellSize + margin), y * (cellSize + margin), matrix(y)(x))

    changed = false
//    var image = offScreenContext.getImageData(0, 0, offscreenCanvas.width, offscreenCanvas.height).asInstanceOf[ImageData]
//    ctx.putImageData(image, 0, 0)
  }

  def clear() : Unit = {
    for ( y <- 0 until height )
      for ( x <- 0 until width )
        matrix(y)(x) = offColorCoeff

    changed = true
  }

  private def set(x: Int, y: Int, color: Double) : Unit = {
    matrix(y)(x) = color
  }

  private def print(x: Int, y: Int, c: Char, font: Font) : Unit = {
    val charFont = font.find(_.char.charAt(0) == c)

    charFont.foreach(cf => print(x, y, toArray(cf), font))
  }

  def print(x: Int, y: Int, s: String, font: Font) : Unit = {
    for (ix <- s.indices)
      print(x + ix * 8, y, s.charAt(ix), font)

    changed = true
  }

  def scrollLeft() : Unit = {
    for ( y <- 0 until height )
      for ( x <- 0 until width) {
        val actualValue = matrix(y)(x)
        val newValue: Double = if (x == width - 1) offColorCoeff else matrix(y)(x + 1)

        if (actualValue != newValue) {
          matrix(y)(x) =
            if (newValue == 1) {
              1
            } else if (actualValue < offColorCoeff) {
              offColorCoeff
            } else {
              actualValue * 0.8
            }

          if (x < width - 1) {
            matrix(y)(x + 1) =
              if (newValue < offColorCoeff) {
                offColorCoeff
              } else {
                newValue * 0.8
              }
          }
          changed = true
        }
      }
  }

  private def toArray(charFont: CharFont) : Array[IndexedSeq[Boolean]] = {
    charFont.bitmap.map(row => row.map(ch => ch == '1'))
  }

  private def print(x: Int, y: Int, map: Array[IndexedSeq[Boolean]], font: Font) : Unit = {
    for (iy <- map.indices)
      for (ix <- map(iy).indices)
        if (map(iy)(ix)) set(x + ix, y + iy, 1)
  }

  private def square(ctx: dom.CanvasRenderingContext2D, x: Int, y: Int, color: Double) {
    ctx.fillStyle = deriveColor(onColorColor, color)
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
