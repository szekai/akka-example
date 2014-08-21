package sky.first.actors

import akka.actor.{Props, ActorRef, Actor}

/**
 * Created by szekai on 20/08/2014.
 */

//case class Word(word:String, count:Int)
case class Result()
case class MapData(dataList: List[(String, Int)])
case class ReduceData(reduceDataMap: Map[String,Int])

class MasterActor extends Actor{
  val aggregateActor: ActorRef = context.actorOf(Props[AggregateActor], name = "aggregate")
  val reduceActor: ActorRef = context.actorOf(Props(classOf[ReduceActor],aggregateActor), name = "reduce")
  val mapActor: ActorRef = context.actorOf(Props(classOf[MapActor],reduceActor), name = "map")

  def receive: Receive = {
    case message: String => mapActor ! message
    case message: Result => aggregateActor ! message
  }
}
