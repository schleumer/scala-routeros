package li.ues.routeros


object Parameters {
  def apply(parameters: (String, String)*) = new Parameters(parameters)
}

class Parameters(parameters: Seq[(String, String)]) {
  implicit def toList: List[String] = parameters.map(v => s"=${v._1}=${v._2}").toList
}