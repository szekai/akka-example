/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sky.supervisor.example1

import akka.actor.Actor
import akka.actor.ActorLogging

class WorkerActor extends Actor with ActorLogging{
  var state: Int = 0

  override def preStart() {
    log.info("Starting WorkerActor instance hashcode # {}", this.hashCode())
  }
  override def postStop() {
    log.info("Stopping WorkerActor instance hashcode # {}", this.hashCode())
  }
  def receive: Receive = {
    case value: Int =>
      if (value <= 0)
        throw new ArithmeticException("Number equal or less than zero")
      else
        state = value
    case result: Result =>
      sender ! state
    case ex: NullPointerException =>
      throw new NullPointerException("Null Value Passed")
    case _ =>
      throw new IllegalArgumentException("Wrong Arguement")
  }
}
