package sky.supervisor.example2

import akka.actor.SupervisorStrategy.{Escalate, Stop, Restart, Resume}
import akka.actor.{AllForOneStrategy, Props, ActorLogging, Actor}
import scala.concurrent.duration._

/**
 * Created by szekai on 19/08/2014.
 */
class SupervisorActor extends Actor with ActorLogging{
  val workerActor1 = context.actorOf(Props[WorkerActor1], name = "workerActor1")
  val workerActor2 = context.actorOf(Props[WorkerActor2], name = "workerActor2")

  override def supervisorStrategy = AllForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 10 seconds) {

    case _: ArithmeticException => Resume
    case _: NullPointerException => Restart
    case _: IllegalArgumentException => Stop
    case _: Exception => Escalate
  }

  def receive = {
    case result: Result =>
      workerActor1.tell(result, sender)
    case msg: Object =>
      workerActor1 ! msg

  }
}
