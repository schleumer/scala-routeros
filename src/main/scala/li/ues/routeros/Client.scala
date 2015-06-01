// NOPE, I'M NOT INSANE, CHECK THIS OUT: http://wiki.mikrotik.com/wiki/Manual:API

package li.ues.routeros

import li.ues.routeros._
import java.net.InetSocketAddress
import java.net.Socket
import java.io._
 
object Client {
  def apply(host: String, port: Integer, user: String = "", password: String = "") {
    if(user.isEmpty) {
      new Client(host, port)
    } else {
      new Client(host, port).login(user, password)
    }
    
  }
}

class Client(host: String, port: Integer) extends Socket(host, port) {
  this.setSoTimeout(2000)

  val in = new DataInputStream(this.getInputStream())
  val out = new DataOutputStream(this.getOutputStream())

  def readLength[T]()(block: Integer => T): T = {
    var a = in.read()
    var sk = 0
    if (a < 0x80) {
      sk = a
    } else if (a < 0xC0) {
      a = a << 8
      a += in.read()
      sk = a ^ 0x8000
    } else if (a < 0xE0) {
      for (i <- 0 until 2) {
        a = a << 8
        a += in.read()
      }
       sk = a ^ 0xC00000
    } else if (a < 0xF0) {
      for (i <- 0 until 3) {
        a = a << 8
        a += in.read()
      }
    }
    block(sk)
  }

  def rawRead(): String = {
    readLength() { len =>
      val byteArray = new Array[Byte](len)
      in.read(byteArray, 0, len)
      byteArray.map(_.toChar).mkString
    }
  }
    

  def rawWrite(data: String) = {
    println(s"Writing $data(${data.length}) to $host:$port")
    out.write(data.getBytes("US-ASCII"))
  }

  private[li] def mecaSuasPalavrasParca(palavra: String) = (palavra.length match {
    case x if x < 0x80 => Seq(x)
    case x if x < 0x4000 => Seq(
      x >> 8 | 0x80,
      x
    )
    case x if x < 0x200000 => Seq(
      x >> 16 | 0xC0,
      x >> 8,
      x
    )
    case x if x < 0x10000000 => Seq(
      x >> 24 | 0xE0,
      x >> 16,
      x >> 8,
      x
    )
    case x => Seq(
      0xF0,
      x >> 24,
      x >> 16,
      x >> 8,
      x
    )
  }).map(_.toChar).mkString + palavra

  def tell(words: String) = {
    rawWrite(mecaSuasPalavrasParca(words))
    rawWrite(mecaSuasPalavrasParca(""))
  }

  // Jorge: Quiere ser hardcore y su mama no lo deja
  def readLines(): Stream[List[String]] = List[String](rawRead()) #:: readLines.map(x => x :+ rawRead())

  def login(user: String, password: String): this.type = {
    tell("/login")
    println(readLines.takeWhile(_.tail != "").toList)
    // <this> is kinda wrong
    this
  }
}