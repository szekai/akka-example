package sky.dispatcher.example.BalancingDispatcher

import akka.actor.{Props, ActorSystem}
import akka.routing.BalancingPool
import com.typesafe.config.ConfigFactory
import sky.MsgEchoActor

/**
 * Created by szekai on 22/08/2014.
 */
object Example1 {
  def main(args: Array[String]): Unit = {
    val _system = ActorSystem.create("balancing-dispatcher",ConfigFactory.load().getConfig("MyDispatcherExample"))

//    val actor = _system.actorOf(Props[MsgEchoActor].withDispatcher("balancingDispatcher").withRouter(
//      RoundRobinRouter(5)))

    val actor = _system.actorOf(BalancingPool(5).props(Props[MsgEchoActor]),"BalancingPool")

    0 to 25 foreach {
      i => actor ! i
    }
    Thread.sleep(3000)
    _system.shutdown()
  }
}
