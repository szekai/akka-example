/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sky.calculator.example3

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.TypedActor
import akka.actor.TypedProps
import akka.pattern.ask
import sky.calculator.CalculatorInt
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._

object CalculatorActorSystem {
  def main(args: Array[String]): Unit = {

    val _system = ActorSystem("TypedActorsExample")

    val calculator: CalculatorInt =
      TypedActor(_system).typedActorOf(TypedProps[SupervisorActor]())

    // Get access to the ActorRef
    val calActor: ActorRef = TypedActor(_system).getActorRefFor(calculator)
    // call actor with a message
    calActor.tell("Hi there", calActor)

    //wait for child actor to get restarted
    Thread.sleep(500)

    // Invoke the method and wait for result
    implicit val timeout = Timeout(5 seconds)
    val future = (calActor ? 10).mapTo[Integer] 
    val result = Await.result(future, timeout.duration)

    println("Result from child actor->" + result)

    //wait before shutting down the system
    Thread.sleep(500)

    _system.shutdown()
  }
}
