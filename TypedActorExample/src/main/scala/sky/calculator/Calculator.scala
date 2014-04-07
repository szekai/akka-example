/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sky.calculator

import akka.actor.ActorRef
import akka.actor.TypedActor
import akka.actor.TypedActor.PostStop
import akka.actor.TypedActor.PreStart
import akka.actor.TypedActor.Receiver
import akka.event.Logging
import scala.concurrent.Future

trait CalculatorInt extends Receiver  {

	def add(first: Int, second: Int): Future[Int]

	def subtract(first: Int, second: Int): Future[Int]

	def incrementCount(): Unit

	def incrementAndReturn(): Option[Int]
}

class Calculator extends CalculatorInt with PreStart with PostStop {

  import TypedActor.context
  var counter: Int = 0
  val log = Logging(context.system, TypedActor.self.getClass())

  import TypedActor.dispatcher

  //Non blocking request response
  def add(first: Int, second: Int): Future[Int] = Future successful first + second
  //Non blocking request response
  def subtract(first: Int, second: Int): Future[Int] = Future successful first - second
  //fire and forget
  def incrementCount(): Unit = counter += 1
  //Blocking request response
  def incrementAndReturn(): Option[Int] = {
    counter += 1
    Some(counter)
  }

  def onReceive(message: Any, sender: ActorRef): Unit = {
    log.info("Message received-> {}", message)
  }

  //Allows to tap into the Actor PreStart hook
  def preStart(): Unit = {
    log.info("Actor Started")
  }
  //Allows to tap into the Actor PostStop hook
  def postStop(): Unit = {
    log.info("Actor Stopped")
  }

}
