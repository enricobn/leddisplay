package org.leddisplay

import utest._

/**
  * Created by enrico on 10/19/16.
  */
object FontImageReaderSpec extends TestSuite {
  val data = scalajs.js.Array[Int]()
  // 1001
  // 0111
  // 1001
  // 1001
  add(data, '1') // 0   0 0 0
  add(data, '0') // 4   0 0 1
  add(data, '0') // 8   1 0 0
  add(data, '1') // 12  1 0 1

  add(data, '0') // 16  0 1 0
  add(data, '1') // 20  0 1 1
  add(data, '1') // 24  1 1 0
  add(data, '1') // 28  1 1 1

  add(data, '1') // 32  2 0 0
  add(data, '0') // 36  2 0 1
  add(data, '0') // 40  3 0 0
  add(data, '1') // 44  3 0 1

  add(data, '1') // 48  2 1 0
  add(data, '0') // 52  2 1 1
  add(data, '0') // 56  3 1 0
  add(data, '1') // 60  3 1 1

  val reader = new FontImageReader(data, 2, 2, (r, g, b, a) => g == 255)

  def tests = TestSuite {
    'read {
      val fonts = reader.read()
      print(fonts.get('\u0000'))
      assert(fonts.get('\u0000').toString.equals("1_\n_1\n"))
      assert(fonts.get('\u0001').toString.equals("_1\n11\n"))
      assert(fonts.get('\u0002').toString.equals("1_\n1_\n"))
      assert(fonts.get('\u0003').toString.equals("_1\n_1\n"))
    }

    'pixelOffset {
      assert(reader.pixelOffset(0, 0, 0) == 0)
      assert(reader.pixelOffset(0, 0, 1) == 4)
      assert(reader.pixelOffset(1, 0, 0) == 8)
      assert(reader.pixelOffset(1, 0, 1) == 12)

      assert(reader.pixelOffset(0, 1, 0) == 16)
      assert(reader.pixelOffset(0, 1, 1) == 20)
      assert(reader.pixelOffset(1, 1, 0) == 24)
      assert(reader.pixelOffset(1, 1, 1) == 28)

      assert(reader.pixelOffset(2, 0, 0) == 32)
      assert(reader.pixelOffset(2, 0, 1) == 36)
      assert(reader.pixelOffset(3, 0, 0) == 40)
      assert(reader.pixelOffset(3, 0, 1) == 44)

      assert(reader.pixelOffset(2, 1, 0) == 48)
      assert(reader.pixelOffset(2, 1, 1) == 52)
      assert(reader.pixelOffset(3, 1, 0) == 56)
      assert(reader.pixelOffset(3, 1, 1) == 60)
    }

    'readPixel {
      assert(reader.readPixel(0, 0, 0))
      assert(!reader.readPixel(0, 0, 1))
      assert(!reader.readPixel(1, 0, 0))
      assert(reader.readPixel(1, 0, 1))

      assert(!reader.readPixel(0, 1, 0))
      assert(reader.readPixel(0, 1, 1))
      assert(reader.readPixel(1, 1, 0))
      assert(reader.readPixel(1, 1, 1))

      assert(reader.readPixel(2, 0, 0))
      assert(!reader.readPixel(2, 0, 1))
      assert(!reader.readPixel(3, 0, 0))
      assert(reader.readPixel(3, 0, 1))

      assert(reader.readPixel(2, 1, 0))
      assert(!reader.readPixel(2, 1, 1))
      assert(!reader.readPixel(3, 1, 0))
      assert(reader.readPixel(3, 1, 1))
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
