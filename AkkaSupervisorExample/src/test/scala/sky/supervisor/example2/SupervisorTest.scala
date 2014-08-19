package sky.supervisor.example2

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
class SupervisorTest (_system: ActorSystem) extends TestKit(_system) with ImplicitSender
with WordSpecLike with Matchers with BeforeAndAfterAll {
  def this() = this(ActorSystem("SupervisorSys", ConfigFactory.load().getConfig("SupervisorSys")))

  "An Child actor" must {

    "send back messages unchanged" in {
      val supervisor = system.actorOf(Props[SupervisorActor])
      supervisor ! 8
      implicit val timeout = Timeout(5 seconds)
      val future = (supervisor ? new Result).mapTo[Int]
      val result = Await.result(future, timeout.duration)
      result should equal (8)
    }

  }

  "An supervisor actor" must {

    "resume child actor" in {
      val supervisor = system.actorOf(Props[SupervisorActor])
      //first send a correct message
      supervisor ! 8
      //Send a  message that generates exception
      supervisor ! -8
      implicit val timeout = Timeout(5 seconds)
      val future = (supervisor ? new Result).mapTo[Int]
      val result = Await.result(future, timeout.duration)
      result should equal (8)
    }

  }

  "An supervisor actor" must {

    "restart child actor" in {
      val supervisor = system.actorOf(Props[SupervisorActor])
      //first send a correct message
      supervisor ! new NullPointerException
      implicit val timeout = Timeout(5 seconds)
      val future = (supervisor ? new Result).mapTo[Int]
      val result = Await.result(future, timeout.duration)
      result should equal (0)
    }

  }

  "An supervisor actor" must {

    "stop child actor" in {
      val supervisor = TestActorRef[SupervisorActor]
      val workerActor1 = supervisor.underlyingActor.workerActor1
      val workerActor2 = supervisor.underlyingActor.workerActor2
      val probe1 = new TestProbe(_system)
      probe1.watch(workerActor1)
      val probe2 = new TestProbe(_system)
      probe2.watch(workerActor2)

      supervisor ! "10L"
      probe1.expectMsgClass(classOf[Terminated])
      probe2.expectMsgClass(classOf[Terminated])
    }

  }
}
