package sky.router.RoundRobinRoutingLogic

import akka.actor.{Actor, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import sky.MsgEchoActor

/**
 * Created by szekai on 22/08/2014.
 */
class Master extends Actor {
  var router = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[MsgEchoActor])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {

    case Terminated(a) => {
      router = router.removeRoutee(a)
      val r = context.actorOf(Props[MsgEchoActor])
      context watch r
      router = router.addRoutee(r)
    }
    case w:Int => router.route(w, sender())
    }
  }
