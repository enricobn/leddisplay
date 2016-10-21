package org.leddisplay

import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.{Event, HTMLImageElement}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by enrico on 10/18/16.
  */
object ImageFont {

  def readFont(onSuccess: (Font) => Unit): Unit = {
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
//    dom.document.body.appendChild(offscreenCanvas)
    image.onload = { evt: Event => {
      offScreenContext.drawImage(image, 0, 0, 8 * 16, 8 * 16)
      val data = offScreenContext.getImageData(0, 0, 8 * 16 , 8 * 16).data
      val reader = new FontImageReader(data, 8, 16, (r, g, b, a) => g == 255)
      val fonts = reader.read()
      onSuccess.apply(fonts)
    }}
    image.src = "src/web/08x08_DOS437_unknown.png"

    //  offScreenContext.fillText(str, 0, 9)

  }

}

class FontImageReader(data: scalajs.js.Array[Int], charSize: Int, columns: Int, onPixel: (Int, Int, Int, Int) => Boolean) {

  def readPixel(char: Int, y: Int, x: Int) : Boolean = {
    val pos = pixelOffset(char, y, x)
    onPixel(data(pos), data(pos + 1), data(pos + 2), data(pos + 3))
  }

  def pixelOffset(char: Int, y: Int, x: Int) : Int =
    ((char / columns) * columns * charSize * charSize + (char % columns) * charSize + y * columns * charSize + x) * 4

  def read() : Font = {
    val fonts = new FontImpl(charSize)

    var i = 0
    var x = 0
    var y = 0
    var ich = 0
    var ch = ich.asInstanceOf[Char]

//    dom.console.log(data.length)

    while (i < data.length) {
      val value = onPixel(data(i), data(i + 1), data(i + 2), data(i + 3))

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
