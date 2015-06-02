import org.specs2.mutable._

import li.ues.routeros._
import java.net.InetSocketAddress

class ClientSpecification extends Specification {
  lazy val client: Client = Client("10.1.1.1", 8728, "admin", "wes_130290")

  "Client" should {
    "connect" in {
      Client("10.1.1.1", 8728, "admin", "wes_130290").must(not(throwA[Exception]))
    }

    "throw exception from wrong connection" in {
      Client("10.1.1.1", 666, "admin", "wes_130290").must(throwA[Exception])
    }

    "throw exception from wrong authentication" in {
      Client("10.1.1.1", 8728, "admin", "").must(throwA[Exception])
    }

    "return a Map[String, String] from command" in {
      client.tell(Command("/ping", Parameters("address" -> "10.2.1.14")))
    }
  }
}