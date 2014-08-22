package sky.router.RoundRobinRoutingLogic

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory

/**
 * Created by szekai on 22/08/2014.
 */
object Example1 {
  def main(args: Array[String]): Unit = {
    val _system = ActorSystem.create("RoundRobin", ConfigFactory.load().getConfig("MyDispatcherExample"))

    val actor = _system.actorOf(Props[Master])

    0 to 25 foreach {
      i => actor ! i
    }
    Thread.sleep(3000)
    _system.shutdown()
  }
}
