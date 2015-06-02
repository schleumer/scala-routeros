// NOPE, I'M NOT INSANE, CHECK THIS OUT: http://wiki.mikrotik.com/wiki/Manual:API

package li.ues.routeros

import li.ues.routeros._
import java.net.InetSocketAddress
import java.net.Socket
import java.io._
import scala.util.matching.Regex
import Implicits._
import scala.util.Random
 
object Client {
  val FeedEnd = ""
  val LoginPath = "/login"
  val Done = "!done"

  def apply(host: String, port: Integer, user: String = "", password: String = ""): Client = {
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

  

  def readLength[T]()(block: Integer => T): T =
    block(in.read() match {
      case x if (x & 0x80) == 0x00 => x
      case x if (x & 0xC0) == 0x80 => ((x & ~0xC0) << 8) + in.read()
      case x if (x & 0xE0) == 0xC0 => 1.to(2).foldLeft(x & ~0xE0)((y, z) => (y << 8) + in.read())
      case x if (x & 0xF0) == 0xE0 => 1.to(3).foldLeft(x & ~0xF0)((y, z) => (y << 8) + in.read())
      case x if (x & 0xF8) == 0xF0 => 1.to(3).foldLeft(in.read())((y, z) => (y << 8) + in.read())
    })
    

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

  def readFromStream(): String = {
    readLength() { len =>
      val byteArray = new Array[Byte](len)
      in.read(byteArray, 0, len)
      byteArray.map(_.toChar).mkString
    }
  }

  def writeToStream(str: String): this.type = { 
    out.write(mecaSuasPalavrasParca(str).getBytes("US-ASCII"));
    this
  }

  def simpleRead: Stream[Data] = Data(readFromStream) #:: simpleRead.takeWhile(!_.raw.isEmpty)

  def rawWrite(data: String, postData: List[String] = List[String](), tag: String = ""): this.type = {
    println(s"Writing $data(${data.length}) to $host:$port")
    val path: List[String] = (List(data, ".tag=" + tag) ++ postData ++ List(""))
    path.foreach(value => writeToStream(value.toString))
    this
  }

  def tell(command: Command) = {
    rawWrite(command.path, command.parameters, scala.util.Random.alphanumeric.take(64).toList.mkString).simpleRead.toList
  }



  def login(user: String, password: String): this.type = {
    tell(Command(Client.LoginPath)).last.value match {
      case hex: String => 
        tell(Command(Client.LoginPath, Parameters(
          "name" -> user,
          "response" -> ("00" + Utils.md5b(0x00, password, Hex(hex)))
        ))).map(_.raw) match {
          case x if !x.contains(Client.Done) => throw new Exception("YaddaYaddaYadda")
          case Nil => throw new Exception("YaddaYaddaYadda")
          case _ => this
        }
      case _ => throw new Exception("YaddaYaddaYadda")
    }
  } 
}