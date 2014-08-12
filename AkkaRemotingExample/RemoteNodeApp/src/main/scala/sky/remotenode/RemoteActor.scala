/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sky.remotenode

import akka.actor.Actor

class RemoteActor extends Actor {
  def receive: Receive = {
    case message: String =>
      // Get reference to the message sender and reply back
      sender ! (message + " got something")
  }
}
