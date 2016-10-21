package org.leddisplay

import org.scalajs.dom
import org.scalajs.dom.html

/**
  * Created by enrico on 10/21/16.
  */
object OffscreenFont {

  def read(fontType: String) : Font = {
    val offscreenCanvas = dom.document.createElement("Canvas").asInstanceOf[html.Canvas]
    offscreenCanvas.width = 32
    offscreenCanvas.height = 32
    val offScreenContext = offscreenCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    val metrics: FontMetrics = fontMetrics(fontType, offscreenCanvas, offScreenContext)
    val font = new FontImpl(metrics.height)

    dom.console.log(metrics.toString)

    offScreenContext.font = fontType

    for (i <- 0 to 255) {
      val c = i.asInstanceOf[Char]
      if (!c.isControl) {
        offScreenContext.fillStyle = "black"
        offScreenContext.fillRect(0, 0, offscreenCanvas.width, offscreenCanvas.height)

        offScreenContext.fillStyle = "white"
        offScreenContext.fillText(c.toString, 0, offscreenCanvas.height / 2)
        val measure = offScreenContext.measureText(c.toString)
        val charFont = font.getOrCreate(c, () => new CharFontImpl(measure.width.toInt))
        val data = offScreenContext.getImageData(0, 0, offscreenCanvas.width, offscreenCanvas.height).data
        for (y <- metrics.minY to metrics.maxY)
          for (x <- 0 until measure.width.toInt) {
            val red = data(y * 4 * offscreenCanvas.width + x * 4)
            val value = getValue(red)
            charFont.set(y - metrics.minY, x, value)
//            charFont.set(y, x, value)
          }
      }
    }

    font

  }

  private case class FontMetrics(var minY: Int, var maxY: Int) {
    def height = maxY - minY + 1

    def update(y: Int): Unit = {
      minY = Math.min(y, minY)
      maxY = Math.max(y, maxY)
    }

    override def toString: String = "(" + minY + ", " + maxY + ", " + height + ")"
  }

  private def fontMetrics(font: String, canvas: html.Canvas, ctx: dom.CanvasRenderingContext2D) : FontMetrics = {
    ctx.fillStyle = "black"
    ctx.fillRect(0, 0, canvas.width, canvas.height)

    ctx.fillStyle = "white"
    ctx.fillText("Hg", 0, canvas.height / 2)

    val data = ctx.getImageData(0, 0, canvas.width, canvas.height).data

    val metrics = new FontMetrics(canvas.height, 0)

    for (y <- 0 until canvas.height)
      for (x <- 0 until canvas.width) {
        val red = data(y * 4 * canvas.width + x * 4)
        val value = getValue(red)
        if (value) {
          metrics.update(y)
        }
      }

    metrics
  }

  private def getValue(red: Int) : Boolean = red > 50


/*  private def fontHeight(font: String) : Int = {
    val text = dom.document.createElement("Span").asInstanceOf[html.Span]
    text.appendChild(dom.document.createTextNode("Hg"))
    text.style.fontFamily = font

    val block = dom.document.createElement("Div").asInstanceOf[html.Div]
    block.style.display = "inline-block"
    block.style.width = "1px"
    block.style.height = "0px"

    val div = dom.document.createElement("Div").asInstanceOf[html.Div]
    div.appendChild(text)
    div.appendChild(block)

    dom.document.body.appendChild(div)
    block.style.verticalAlign = "baseline"

    try {
      block.offset().top - text.offset().top
    } finally {

    }
      try {

        var result = {};

        block.css({ verticalAlign: 'baseline' });
        result.ascent = block.offset().top - text.offset().top;

        block.css({ verticalAlign: 'bottom' });
        result.height = block.offset().top - text.offset().top;

        result.descent = result.height - result.ascent;

      } finally {
        div.remove();
      }

      return result;

  }
*/
}
