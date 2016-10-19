package tutorial.webbapp

import tutorial.webapp.FontImageReader
import utest._

/**
  * Created by enrico on 10/19/16.
  */
object FontImageReaderSpec extends TestSuite {

  def tests = TestSuite {
    'HelloWorld {
      val data = scalajs.js.Array[Int]()
      // 1001
      // 0111
      // 1001
      // 1001
      add(data, '1')
      add(data, '0')
      add(data, '0')
      add(data, '1')

      add(data, '0')
      add(data, '1')
      add(data, '1')
      add(data, '1')

      add(data, '1')
      add(data, '0')
      add(data, '0')
      add(data, '1')

      add(data, '1')
      add(data, '0')
      add(data, '0')
      add(data, '1')

      val reader = new FontImageReader(data, 2, 2)
      val fonts = reader.read()
      assert(fonts.get('\0').toString.equals("10\n01\n"))
      assert(fonts.get('\1').toString.equals("01\n11\n"))
      assert(fonts.get('\2').toString.equals("10\n10\n"))
      assert(fonts.get('\3').toString.equals("01\n01\n"))
    }
  }

  def add(data: scalajs.js.Array[Int], value: Char): Unit = {
    if (value == '1') {
      data.push(255, 255, 255, 0)
    } else {
      data.push(255, 0, 255, 0)
    }
  }
}
