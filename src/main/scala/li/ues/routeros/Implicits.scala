package li.ues.routeros

import scala.language.implicitConversions

object Implicits {
  implicit def parametersToList(parameters: Parameters): List[String] = parameters.toList
  // The lazyness
  implicit def fromHexToBytes(hex: Hex): Array[Byte] = hex.value.sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)
  implicit def fromStringToBytes(str: String): Array[Byte] = str.getBytes
  implicit def fromIntToBytes(i: Int): Array[Byte] = Array[Byte](i.toByte)
} 