package tutorial.webapp

import japgolly.scalajs.react.ReactElement
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.collection.mutable

/**
  * Created by enrico on 9/3/16.
  */
class LedDisplay(cellSize : Int, margin : Int, width : Int, height : Int) {
  val black = "000000"
  var matrix = new Array[Array[String]](height)

  for (y <- 0 until height)
    matrix(y) = new Array[String](width)

  for ( y <- 0 until height )
    for ( x <- 0 until width )
      matrix(y)(x) = black

  def show() : ReactElement = {
    var list = new mutable.MutableList[ReactElement]
      for ( y <- 0 until height )
        for ( x <- 0 until width )
          list += square(x * (cellSize + margin), y * (cellSize + margin), matrix(y)(x))
    <.div(list).render
  }

  def set(x: Int, y: Int, color: String) : Unit = {
    matrix(y)(x) = color
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
