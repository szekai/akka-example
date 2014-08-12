/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sky.localnode

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.util.Timeout
import scala.concurrent.Await
import akka.actor.Props
import akka.pattern.ask
import scala.concurrent.duration._
import sky.remotenode.RemoteActor

class LocalActor2 extends Actor with ActorLogging {

  //Get a reference to the remote actor
  val remoteActor = context.actorOf(Props[RemoteActor], "sampleActor")
  
  implicit val timeout = Timeout(5 seconds)
  def receive: Receive = {
    case message: String =>
      val future = (remoteActor ? message).mapTo[String]
      val result = Await.result(future, timeout.duration)
      log.info("Message received from Server 2 -> {}", result)
  }
}
