import org.specs2.mutable._

import li.ues.routeros._
import java.net.InetSocketAddress

class ClientSpecification extends Specification {
  "Client Specification" should {
    
    "Connect" in {
      Client("10.1.1.1", 8728, "admin", "wes_130290").must(not(throwA[Exception]))
    }

    "Throw excepetion from wrong connection" in {
      Client("10.1.1.1", 666, "admin", "wes_130290").must(throwA[Exception])
    }
  }
}