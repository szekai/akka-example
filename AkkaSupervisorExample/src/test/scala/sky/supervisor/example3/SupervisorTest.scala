package sky.supervisor.example3

import akka.actor.{Terminated, Props, ActorSystem}
import akka.testkit.{TestProbe, TestActorRef, ImplicitSender, TestKit}
import akka.util.Timeout
import akka.pattern.ask
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import scala.concurrent.duration._
import scala.concurrent.Await

/**
 * Created by szekai on 19/08/2014.
 */
class SupervisorTest  (_system: ActorSystem) extends TestKit(_system) with ImplicitSender
with WordSpecLike with Matchers with BeforeAndAfterAll {
  def this() = this(ActorSystem("SupervisorSys", ConfigFactory.load().getConfig("SupervisorSys")))

  "An Monitor actor" must {

    "restart a stopped child actor" in {
      val supervisor = TestActorRef[SupervisorActor]
      val workerActor = supervisor.underlyingActor.childActor
      val probe = new TestProbe(_system)
      probe watch workerActor
      supervisor ! "10"

      probe expectMsgClass(classOf[Terminated])

      Thread.sleep(2000)
      // the actor should get restarted
      // lets send a new value and retrieve the same
      supervisor ! 10
      implicit val timeout = Timeout(5 seconds)
      val future = (supervisor ? new Result).mapTo[Int]
      val result = Await.result(future, timeout.duration)
      result should equal (10)
    }

  }
}
