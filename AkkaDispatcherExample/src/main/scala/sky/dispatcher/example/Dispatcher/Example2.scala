package sky.dispatcher.example.Dispatcher

import akka.actor.{Props, ActorSystem}
import akka.routing.RoundRobinRouter
import com.typesafe.config.ConfigFactory
import sky.MsgEchoActor

/**
 * Created by szekai on 22/08/2014.
 */
object Example2 {
  def main(args: Array[String]): Unit = {
    val _system = ActorSystem.create("default-dispatcher",ConfigFactory.load().getConfig("MyDispatcherExample"))

    val actor = _system.actorOf(Props[MsgEchoActor].withDispatcher("defaultDispatcher1").withRouter(
      RoundRobinRouter(5)))

    0 to 25 foreach {
      i => actor ! i
    }
    Thread.sleep(3000)
    _system.shutdown()
  }
}