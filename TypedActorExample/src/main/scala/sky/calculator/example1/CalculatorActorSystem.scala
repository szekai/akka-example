/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sky.calculator.example1

import akka.actor._
import scala.concurrent.{ExecutionContext,Await}
import sky.calculator._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

object CalculatorActorSystem {

  def main(args: Array[String]): Unit = {

    val _system = ActorSystem("TypedActorsExample")

    val calculator: CalculatorInt =
      TypedActor(_system).typedActorOf(TypedProps[Calculator]())

    calculator.incrementCount()

    // Invoke the method and wait for result
    val future = calculator.add(14, 6)
    var result = Await.result(future, 5 second)
    println("Result is " + result)

    // Invoke the method and wait for result
    var counterResult = calculator.incrementAndReturn()
    println("Result is " + counterResult.get)

    counterResult = calculator.incrementAndReturn()
    println("Result is " + counterResult.get)

    // Get access to the ActorRef
    val calActor: ActorRef = TypedActor(_system).getActorRefFor(calculator)
    // call actor with a message
    calActor ! "Hi there"

    _system.shutdown()
  }

}
