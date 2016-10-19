package tutorial.webapp

import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.{Event, HTMLImageElement}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by enrico on 10/18/16.
  */
class OffscreenFont {
//  var str = ""
//  for (i <- 0 to 255) {
//    val c = i.asInstanceOf[Char]
//    if (!c.isControl) {
//      str += c
//    }
//  }

  val offscreenCanvas = dom.document.createElement("Canvas").asInstanceOf[html.Canvas]
  offscreenCanvas.width = 8 * 16
  offscreenCanvas.height = 8 * 16
  val offScreenContext = offscreenCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
//  offScreenContext.fillStyle = "black"
//  offScreenContext.fillRect(0, 0, 10 * str.length, 10)
//  offScreenContext.fillStyle = "white"
//  offScreenContext.font = "10px Monospace"


  val image = dom.document.createElement("img").asInstanceOf[HTMLImageElement]
  dom.document.body.appendChild(offscreenCanvas)
  image.width = 8 * 16
  image.height = 8 * 16
  image.onload = { evt: Event => {
    offScreenContext.drawImage(image, 0, 0, 8 * 16, 8 * 16)
    getFonts
  }}
  image.src = "src/web/08x08_DOS437_unknown.png"


  /*for (i <- str.indices) {
    offScreenContext.fillText(str.charAt(i).toString, i * 10 + 1, 9)
  }
  */
//  offScreenContext.fillText(str, 0, 9)

  def getFonts : OFonts =  {
    val data = offScreenContext.getImageData(0, 0, 8 * 16 , 8 * 16).data

    val startOf0 = 8//48 * 8 * 8

    var s = ""
    for (y <- 0 until 8) {
      for (x <- 0 until 8) {
        val pos = startOf0 + y * 8 * 16 + x * 4 + 1
        if (data(pos) == 255) {
          s += "1"
        } else {
          s += "0"
        }
      }
      s += "\n"
    }

    dom.console.log(s)

    val reader = new FontImageReader(data, 8, 16)

    val p = reader.readPixel(48, 0, 0)

    val fonts = reader.read()

    dom.console.log(fonts.get('0').toString)

    fonts
  }

}

class FontImageReader(data: scalajs.js.Array[Int], charSize: Int, columns: Int) {

  def readPixel(char: Int, y: Int, x: Int) : Boolean = {
    val pos = pixelOffset(char, y, x)
    data(pos + 1) == 255
  }

  def pixelOffset(char: Int, y: Int, x: Int) : Int =
    ((char / columns) * columns * charSize * charSize + (char % columns) * charSize + y * columns * charSize + x) * 4

  def read() : OFonts = {
    val fonts = new OFonts

    var i = 0
    var x = 0
    var y = 0
    var ich = 0
    var ch = ich.asInstanceOf[Char]

//    dom.console.log(data.length)

    while (i < data.length) {
      val value = data(i) == 255 && data(i + 1) == 255 && data(i + 2) == 255

      if (!ch.isControl) {
//        dom.console.log(ch + ": " + value)
        //        if (value) {
        //          dom.console.log("true: " + ch)
        //        } else {
        //          dom.console.log("false: " + ch)
        //        }
      }

      fonts.get(ch).set(y, x, value)
      i += 4
      if (i < data.length) {
        x += 1
        if (x == charSize) {
          x = 0
          ich += 1
          if (ich % columns == 0) {
            y += 1
            if (y == charSize) {
              y = 0
              ich = (ich / columns) * columns
            } else {
              ich = (ich / columns - 1) * columns
            }
//            if (!ch.isControl) {
//              dom.console.log(ich)
//            }
          }
        }
        ch = ich.asInstanceOf[Char]
      }
    }

//    dom.console.log(fonts.get('0').toString)

    fonts
  }
}

class OFont {
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
        if (data(y)(x)) s += "1" else s += "0"
      s += "\n"
    }
    s
  }

}

class OFonts {
  val data = new mutable.HashMap[Char, OFont]()

  def get(c: Char) : OFont = {
    var font: OFont = null
    if (!data.contains(c)) {
      font = new OFont()
      data(c) = font
    } else {
      font = data(c)
    }
    font
  }

}
