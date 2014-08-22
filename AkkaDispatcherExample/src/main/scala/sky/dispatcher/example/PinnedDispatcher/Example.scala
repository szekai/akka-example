package sky.dispatcher.example.PinnedDispatcher

import akka.actor.{Props, ActorSystem}
import akka.routing.RoundRobinRouter
import com.typesafe.config.ConfigFactory
import sky.MsgEchoActor

/**
 * Created by szekai on 22/08/2014.
 */
object Example {
  def main(args: Array[String]): Unit = {
    val _system = ActorSystem.create("pinned-dispatcher",ConfigFactory.load().getConfig("MyDispatcherExample"))

    val actor = _system.actorOf(Props[MsgEchoActor].withDispatcher("pinnedDispatcher").withRouter(
      RoundRobinRouter(5)))

    0 to 25 foreach {
      i => actor ! i
    }
    Thread.sleep(3000)
    _system.shutdown()
  }
}
