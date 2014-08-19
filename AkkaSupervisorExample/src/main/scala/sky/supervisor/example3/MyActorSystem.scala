package sky.supervisor.example3

import akka.actor.{ActorRef, Props, ActorSystem}

/**
 * Created by szekai on 19/08/2014.
 */
case class Result()
case class DeadWorker()
case class RegisterWorker(val worker: ActorRef, val supervisor: ActorRef)
object MyActorSystem {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("faultTolerance")

    val supervisor = system.actorOf(Props[SupervisorActor], name = "supervisor")

    supervisor ! 8

    supervisor ! "Do Something"

    Thread.sleep(4000)

    supervisor ! 8

    system.shutdown
  }
}
