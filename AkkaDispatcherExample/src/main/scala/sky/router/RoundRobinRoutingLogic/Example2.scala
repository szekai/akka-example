package sky.router.RoundRobinRoutingLogic

import akka.actor.{Props, ActorSystem}
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory
import sky.MsgEchoActor

/**
 * Created by szekai on 22/08/2014.
 */
object Example2 {
  def main(args: Array[String]): Unit = {
    val _system = ActorSystem.create("RoundRobin", ConfigFactory.load("router"))
    val actor = _system.actorOf(FromConfig.props(Props[MsgEchoActor]),"router1")

    0 to 25 foreach {
      i => actor ! i
    }
    Thread.sleep(3000)

    _system.shutdown()
  }
}
