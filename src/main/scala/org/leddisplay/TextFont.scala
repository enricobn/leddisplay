package org.leddisplay

import org.scalajs.dom

import upickle.default._

/**
  * Created by enrico on 9/3/16.
  */

object TextFont {
  type FontType = Array[TextCharFont]

  def readFont(url: String, onSuccess: (Font) => Unit) : Unit = {
    // for Ajax call
    import dom.ext._
    import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    Ajax.get(url).onSuccess{ case xhr =>
      val font = read[FontType](xhr.responseText)
      onSuccess(new TextFont(font))
    }
  }
}

class TextFont(data: Array[TextCharFont]) extends Font {
  override def get(c: Char): TextCharFont = {
    // TODO if I cannot find it?
    data.filter(ch => ch.char == c.toString)(0)
//    data(c.asInstanceOf[Int])
  }

  override def size: Int = 8
}

case class TextCharFont(char: String, bitmap: Array[String]) extends CharFont {
  val width = bitmap(0).length

  def set(y: Int, x: Int, value: Boolean) {
    throw new UnsupportedOperationException
  }

  override def get(y: Int, x: Int): Boolean = {
    bitmap(y).charAt(x) == '1'
  }
}

