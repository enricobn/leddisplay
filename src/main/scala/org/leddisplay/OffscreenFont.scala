package org.leddisplay

import org.scalajs.dom
import org.scalajs.dom.html

/**
  * Created by enrico on 10/21/16.
  */
object OffscreenFont {

  def read(fontFamily: String, size: Int, colorThreshold: Int) : Font = {
    val canvas = dom.document.createElement("Canvas").asInstanceOf[html.Canvas]
    canvas.width = size * 4 // it must contain 2 chars
    canvas.height = size * 2
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    ctx.font = size + "px " + fontFamily

    val metrics: FontMetrics = print("Hg", canvas, ctx, colorThreshold)
    val font = new FontImpl(metrics.height)

    for (i <- 0 to 255) {
      val c = i.asInstanceOf[Char]
      if (!c.isControl) {
        ctx.fillStyle = "black"
        ctx.fillRect(0, 0, canvas.width, canvas.height)

        ctx.fillStyle = "white"
        ctx.fillText(c.toString, 0, canvas.height / 2)
        val measure = ctx.measureText(c.toString)

        val charMetrics: FontMetrics = print(c.toString, canvas, ctx, colorThreshold)

        val charWitdh =
          if (charMetrics.width <= 0) {
            measure.width.toInt
          } else {
            charMetrics.width
          }

        val charFont = font.getOrCreate(c, () => new CharFontImpl(charWitdh))
        for (y <- metrics.minY to metrics.maxY)
          for (x <- charMetrics.minX to charMetrics.maxX) {
            val red = charMetrics.data(y * 4 * canvas.width + x * 4)
            val value = red > colorThreshold
            charFont.set(y - metrics.minY, x - charMetrics.minX, value)
          }
      }
    }

    font

  }

  private case class FontMetrics(data: scala.scalajs.js.Array[Int], var minY: Int, var maxY: Int, var minX: Int, var maxX: Int) {
    def height = maxY - minY + 1
    def width = maxX - minX + 1

    def update(x: Int, y: Int): Unit = {
      minY = Math.min(y, minY)
      maxY = Math.max(y, maxY)
      minX = Math.min(x, minX)
      maxX = Math.max(x, maxX)
    }

    override def toString: String = "(" + minX + "-" + maxX + ", " + minY + "-" + maxY + ", " + width + ", " + height + ")"
  }

  private def print(text: String, canvas: html.Canvas, ctx: dom.CanvasRenderingContext2D, colorThreshold: Int) : FontMetrics = {
    ctx.fillStyle = "black"
    ctx.fillRect(0, 0, canvas.width, canvas.height)

    ctx.fillStyle = "white"
    ctx.fillText(text, canvas.width / 2, canvas.height / 2)

    val data = ctx.getImageData(0, 0, canvas.width, canvas.height).data

    val metrics = new FontMetrics(data, canvas.height, 0, canvas.width, 0)

    for (y <- 0 until canvas.height)
      for (x <- 0 until canvas.width) {
        val red = data(y * 4 * canvas.width + x * 4)
        val value = red > colorThreshold
        if (value) {
          metrics.update(x, y)
        }
      }

    metrics
  }

}
