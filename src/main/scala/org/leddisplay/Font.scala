package org.leddisplay

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by enrico on 10/21/16.
  */
trait Font {

  def get(c: Char) : CharFont

  def size: Int

}

trait CharFont {

  def get(y: Int, x: Int) : Boolean

  def width: Int

}

class CharFontImpl(val width: Int) extends CharFont {
  val data = new mutable.ArrayBuffer[mutable.ArrayBuffer[Boolean]]()

  def set(y: Int, x: Int, value: Boolean): Unit = {
    while (data.length <= y)
      data += new ArrayBuffer[Boolean]()
    var row = data(y)
    while (row.length <= x)
      row += false
    row(x) = value
  }

  override def toString = {
    var s = ""
    for (y <- data.indices) {
      for (x <- data(y).indices)
        if (data(y)(x)) s += "1" else s += " "
      s += "\n"
    }
    s
  }

  override def get(y: Int, x: Int): Boolean = data(y)(x)

}

class FontImpl(val size: Int) extends Font {
  val data = new mutable.HashMap[Char, CharFontImpl]()

  override def get(c: Char) : CharFontImpl = {
    data(c)
  }

  def getOrCreate(c: Char, supplier: () => CharFontImpl) : CharFontImpl = {
    if (!data.contains(c)) {
      val font = supplier.apply()
      data(c) = font
      font
    } else {
      data(c)
    }
  }

}
