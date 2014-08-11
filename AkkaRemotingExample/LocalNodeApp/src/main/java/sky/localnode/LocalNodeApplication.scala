/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sky.localnode
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props
object LocalNodeApplication {

  def main(args: Array[String]): Unit = {
    // load the configuration
    val config = ConfigFactory.load().getConfig("LocalSys")
    val system = ActorSystem("LocalNodeApp", config)
    val clientActor = system.actorOf(Props[LocalActor])
    clientActor ! "Hello"
    Thread.sleep(4000)
    system.shutdown()
  }
}
