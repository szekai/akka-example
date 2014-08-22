package first.actors

import akka.actor.{ActorLogging, Actor, ActorRef}

/**
 * Created by szekai on 20/08/2014.
 */
class MapActor (reduceActor: ActorRef) extends Actor with ActorLogging{

  override def preStart() {
    log.info("Starting MapActor instance hashcode # {}", this.hashCode())
  }
  override def postStop() {
    log.info("Stopping MapActor instance hashcode # {}", this.hashCode())
  }

  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be",
    "do", "go", "if", "in", "is", "it", "of", "on", "the", "to")

  val defaultCount: Int = 1

  def receive: Receive = {
    case message: String =>
      reduceActor ! evaluateExpression(message)
  }
  def evaluateExpression(line: String): MapData = {
    val dataList = line.toLowerCase().split("(\\s+)").filter(x => !x.isEmpty && !STOP_WORDS_LIST.contains(x)).map(x => x -> defaultCount).toList
    return new MapData(dataList)
  }
}