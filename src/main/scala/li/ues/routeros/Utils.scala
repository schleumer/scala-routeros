package li.ues.routeros

import java.security.MessageDigest

object Hex {
  def apply(str: String) = new Hex(str)
}

class Hex(val value: String) {

}

object Utils {
  def md5(value: String*): String = MessageDigest
    .getInstance("MD5")
    .digest(
      value
        .map(_.getBytes)
        .reduceLeft((left, right) => left ++ right))
    .map("%02X".format(_))
    .mkString
    .toLowerCase()

  def md5b(value: Array[Byte]*): String = MessageDigest
    .getInstance("MD5")
    .digest(
      value
        .reduceLeft((left, right) => left ++ right))
    .map("%02X".format(_))
    .mkString
    .toLowerCase()
}