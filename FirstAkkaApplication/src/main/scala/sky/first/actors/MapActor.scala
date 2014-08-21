package sky.first.actors

import akka.actor.{Actor, ActorRef}

/**
 * Created by szekai on 20/08/2014.
 */
class MapActor (reduceActor: ActorRef) extends Actor {

  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be",
    "do", "go", "if", "in", "is", "it", "of", "on", "the", "to")

  val defaultCount: Int = 1

  def receive: Receive = {
    case message: String =>
      reduceActor ! evaluateExpression(message)
  }
  def evaluateExpression(line: String): MapData = {
    val dataList = line.toLowerCase().split("(\\s+)").filter(x => !x.isEmpty && !STOP_WORDS_LIST.contains(x)).map(x => x -> 1).toList
    return new MapData(dataList)
  }
}