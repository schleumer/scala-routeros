package li.ues.routeros

object Command {
  def apply(path: String) = new Command(path, Parameters())
  def apply(path: String, parameters: Parameters) = new Command(path, parameters)
}

class Command(val path: String, val parameters: Parameters) {
  
}