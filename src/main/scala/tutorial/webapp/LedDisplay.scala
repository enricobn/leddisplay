package tutorial.webapp

import japgolly.scalajs.react.vdom.prefix_<^._

import scala.collection.mutable

/**
  * Created by enrico on 9/3/16.
  */
class LedDisplay(cellSize : Int, margin : Int, width : Int, height : Int) {
  private val sCellSize = s"${cellSize}px".intern()
  private val matrix = new Array[Array[String]](height)

  val black = "000000"

  for (y <- 0 until height)
    matrix(y) = new Array[String](width)

  clear()

  def show() : ReactTag = {
    var list = new mutable.MutableList[ReactTag]
      for ( y <- 0 until height )
        for ( x <- 0 until width )
          list += square(x * (cellSize + margin), y * (cellSize + margin), matrix(y)(x))
    <.div(list)
  }

  def clear() : Unit = {
    for ( y <- 0 until height )
      for ( x <- 0 until width )
        matrix(y)(x) = black
  }

  def set(x: Int, y: Int, color: String) : Unit = {
    matrix(y)(x) = color
  }

  def print(x: Int, y: Int, c: Char, font: Font, color: String) : Unit = {
    val charFont = font.get(c)

    print(x, y, charFont, font.size, color)
  }

  def print(x: Int, y: Int, s: String, font: Font, color: String) : Unit = {
    for (ix <- s.indices)
      print(x + ix * font.size, y, s.charAt(ix), font, color)
  }

  def scrollLeft() : Unit = {
    for ( y <- 0 until height )
      for ( x <- 0 until width)
        matrix(y)(x) = if (x == width -1) black else matrix(y)(x + 1)
  }

  private def print(x: Int, y: Int, charFont: CharFont, size: Int, color: String) : Unit = {
    for (iy <- 0 until size)
      for (ix <- 0 until size)
        if (charFont.get(iy, ix)) set(x + ix, y + iy, color)
  }

  private def square(x: Int, y: Int, color: String) : ReactTag = {
    <.div(^.backgroundColor := "#" + color
      , ^.width := sCellSize
      , ^.height := sCellSize
      , ^.position := "absolute"
      , ^.left := x + "px"
      , ^.top := y + "px"
    )
  }

}
