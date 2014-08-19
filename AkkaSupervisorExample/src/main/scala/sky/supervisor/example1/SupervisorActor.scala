package sky.supervisor.example1

import akka.actor._
import sky.supervisor.example1.WorkerActor
import scala.concurrent.duration._

class SupervisorActor extends Actor with ActorLogging{
  import akka.actor.SupervisorStrategy._
  val childActor = context.actorOf(Props[WorkerActor], name = "workerActor")

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 10 seconds){
    case _: ArithmeticException => Resume
    case _: NullPointerException => Restart
    case _: IllegalArgumentException => Stop
    case _: Exception => Escalate
  }

  def receive = {
    case result: Result => childActor.tell(result, sender)
    case msg: Object => childActor ! msg
  }
}