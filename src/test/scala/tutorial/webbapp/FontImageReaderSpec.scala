package tutorial.webbapp

import org.scalatest.FlatSpec
import tutorial.webapp.FontImageReader

/**
  * Created by enrico on 10/19/16.
  */
class FontImageReaderSpec extends FlatSpec{
  "Reading" should "find the right path for example 1" in {
    val data = scalajs.js.Array(255, 255, 255, 255)
    val reader = new FontImageReader(data)
    val fonts = reader.read()

  }
}
