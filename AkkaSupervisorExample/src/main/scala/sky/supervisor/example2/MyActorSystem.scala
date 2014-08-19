package sky.supervisor.example2

import akka.actor.{Props, ActorSystem}
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * Created by szekai on 19/08/2014.
 */

case class Result()

object MyActorSystem {
  def main(args: Array[String]): Unit ={
    val system = ActorSystem("faultTolerance")
    val log = system.log

    val supervisor = system.actorOf(Props[SupervisorActor], name = "supervisor")

    log.info("Sending value 8, no exceptions should be thrown! ")
    supervisor ! 8

    implicit val timeout = Timeout(5 seconds)
    var future = (supervisor ? new Result).mapTo[Int]
    var result = Await.result(future, timeout.duration)

    log.info("Value Received-> {}", result)

    log.info("Sending value -8, ArithmeticException should be thrown! Our Supervisor strategy says resume !")
    supervisor ! -8

    future = (supervisor ? new Result).mapTo[Int]
    result = Await.result(future, timeout.duration)

    log.info("Value Received-> {}", result)

    log.info("Sending value null, NullPointerException should be thrown! Our Supervisor strategy says restart !")
    supervisor ! new NullPointerException

    future = (supervisor ? new Result).mapTo[Int]
    result = Await.result(future, timeout.duration)

    log.info("Value Received-> {}", result)

    log.info("Sending value \"String\", IllegalArgumentException should be thrown! Our Supervisor strategy says Stop !")

    supervisor ? "Do Something"

    log.info("Worker Actors shutdown !")

    system.shutdown
  }
}
