package sky.supervisor.example1

import akka.actor._
import akka.testkit.{TestActorRef, TestProbe, ImplicitSender, TestKit}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory
import org.scalatest.{WordSpecLike, BeforeAndAfterAll, Matchers}

/**
 * Created by szekai on 19/08/2014.
 */
class SupervisorTest(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
with WordSpecLike with Matchers with BeforeAndAfterAll {
  def this() = this(ActorSystem("SupervisorSys", ConfigFactory.load().getConfig("SupervisorSys")))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

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
      val workerActor = supervisor.underlyingActor.childActor
      val probe = new TestProbe(_system)
      probe.watch(workerActor)

      supervisor ! "10L"
      probe.expectMsgClass(classOf[Terminated])
    }

  }
}
