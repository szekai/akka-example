/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sky.calculator.example2

import akka.actor.ActorSystem
import akka.actor._
import akka.routing.BroadcastGroup
import sky.calculator._

object CalculatorActorSystem {
  def main(args: Array[String]): Unit = {

    val _system = ActorSystem("TypedActorsExample")

    val calculator1: CalculatorInt =
      TypedActor(_system).typedActorOf(TypedProps[Calculator]())

    val calculator2: CalculatorInt =
      TypedActor(_system).typedActorOf(TypedProps[Calculator]())

    // Create a router with Typed Actors
    val actor1: ActorRef = TypedActor(_system).getActorRefFor(calculator1)
    val actor2: ActorRef = TypedActor(_system).getActorRefFor(calculator2)

    val routees = Vector[ActorRef](actor1, actor2)

    val paths = List(actor1.path.toString, actor2.path.toString)
    val router: ActorRef =
      _system.actorOf(BroadcastGroup(paths).props(), "router")
    router ! "Hello there"

    _system.shutdown()
  }
}
