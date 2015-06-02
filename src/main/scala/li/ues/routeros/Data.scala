package li.ues.routeros
import scala.util.matching.Regex

object Data {
  def apply(raw: String) = new Data(raw)
}

class Data(val raw: String) {
  val keyValueMatcher = """^=(.*?)=(.*?)$""".r
  
  lazy val value = raw match {
    case keyValueMatcher(_, x) => x
    case _ => null
  }

  lazy val key = raw match {
    case keyValueMatcher(x, _) => x
    case _ => null
  }

  def isRe = raw == "!re"
  def isTermination = raw == ""
  def isDone = raw == "!done"
  override def toString = s"Data($raw)"
}