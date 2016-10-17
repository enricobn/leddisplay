package tutorial.webapp

import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.ImageData
import tutorial.webapp.Font.Font

import scala.collection.immutable.IndexedSeq

/**
  * Created by enrico on 9/3/16.
  */
class LedDisplayCanvas(ctx: dom.CanvasRenderingContext2D, cellSize : Int, margin : Int, width : Int, height : Int,
                       offColor: String, onColor: String) {
  private val matrix = new Array[Array[String]](height)

  for (y <- 0 until height)
    matrix(y) = new Array[String](width)

  clear()

//  val offscreenCanvas = dom.document.createElement("Canvas").asInstanceOf[html.Canvas]
//  offscreenCanvas.width = width * (cellSize + margin)
//  offscreenCanvas.height = height * (cellSize + margin)
//  val offScreenContext = offscreenCanvas.getContext("2d")

  def show() {
      for ( y <- 0 until height )
        for ( x <- 0 until width )
          square(ctx, x * (cellSize + margin), y * (cellSize + margin), matrix(y)(x))
//    var image = offScreenContext.getImageData(0, 0, offscreenCanvas.width, offscreenCanvas.height).asInstanceOf[ImageData]
//    ctx.putImageData(image, 0, 0)
  }

  def clear() : Unit = {
    for ( y <- 0 until height )
      for ( x <- 0 until width )
        matrix(y)(x) = offColor
  }

  def set(x: Int, y: Int, color: String) : Unit = {
    matrix(y)(x) = color
  }

  def print(x: Int, y: Int, c: Char, font: Font) : Unit = {
    val charFont = font.find(_.char.charAt(0) == c)

    charFont.foreach(cf => print(x, y, toArray(cf), font))
  }

  def print(x: Int, y: Int, s: String, font: Font) : Unit = {
    for (ix <- s.indices)
      print(x + ix * 8, y, s.charAt(ix), font)
  }

  def scrollLeft() : Unit = {
    for ( y <- 0 until height )
      for ( x <- 0 until width)
        matrix(y)(x) = if (x == width -1) offColor else matrix(y)(x + 1)
  }

  private def toArray(charFont: CharFont) : Array[IndexedSeq[Boolean]] = {
    charFont.bitmap.map(row => row.map(ch => ch == '1'))
  }

  private def print(x: Int, y: Int, map: Array[IndexedSeq[Boolean]], font: Font) : Unit = {
    for (iy <- map.indices)
      for (ix <- map(iy).indices)
        if (map(iy)(ix)) set(x + ix, y + iy, onColor)
  }

  private def square(ctx: dom.CanvasRenderingContext2D, x: Int, y: Int, color: String) {
    ctx.fillStyle = color
    ctx.fillRect(x, y, cellSize, cellSize)
  }

}
