package tutorial.webapp

import japgolly.scalajs.react.ReactElement
import japgolly.scalajs.react.vdom.prefix_<^._
import tutorial.webapp.Font.Font

import scala.collection.immutable.IndexedSeq
import scala.collection.mutable

/**
  * Created by enrico on 9/3/16.
  */
class LedDisplay(cellSize : Int, margin : Int, width : Int, height : Int) {
  val black = "000000"
  private var matrix = new Array[Array[String]](height)

  for (y <- 0 until height)
    matrix(y) = new Array[String](width)

  clear()

  def show() : ReactElement = {
    var list = new mutable.MutableList[ReactElement]
      for ( y <- 0 until height )
        for ( x <- 0 until width )
          list += square(x * (cellSize + margin), y * (cellSize + margin), matrix(y)(x))
    <.div(list).render
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
    val charFont = font.find(_.char.charAt(0) == c)

    charFont.foreach(cf => print(x, y, toArray(cf), font, color))
  }

  def print(x: Int, y: Int, s: String, font: Font, color: String) : Unit = {
    for (ix <- s.indices)
      print(x + ix * 8, y, s.charAt(ix), font, color)
  }

  def scrollLeft() : Unit = {
    for ( y <- 0 until height )
      for ( x <- 0 until width)
        matrix(y)(x) = if (x == width -1) black else matrix(y)(x + 1)
  }

  private def toArray(charFont: CharFont) : Array[IndexedSeq[Boolean]] = {
    charFont.bitmap.map(row => row.map(ch => ch == '1'))
  }

  private def print(x: Int, y: Int, map: Array[IndexedSeq[Boolean]], font: Font, color: String) : Unit = {
    for (iy <- map.indices)
      for (ix <- map(iy).indices)
        if (map(iy)(ix)) set(x + ix, y + iy, color)
  }

  private def square(x: Int, y: Int, color: String) : ReactElement = {
    <.div(^.backgroundColor := s"#$color"
      , ^.width := s"${cellSize}px"
      , ^.height := s"${cellSize}px"
      , ^.position := "absolute"
      , ^.left := s"${x}px"
      , ^.top := s"${y}px"
    ).render
  }

}
