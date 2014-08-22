package first.actors

import akka.actor.{ActorLogging, Actor}

import scala.collection.mutable

/**
 * Created by szekai on 20/08/2014.
 */
class AggregateActor extends Actor with ActorLogging{
  override def preStart() {
    log.info("Starting AggregateActor instance hashcode # {}", this.hashCode())
  }
  override def postStop() {
    log.info("Stopping AggregateActor instance hashcode # {}", this.hashCode())
  }

  var finalReducedMap = new mutable.HashMap[String, Int]

  def receive: Receive = {
    case message: ReduceData => aggregateInMemoryReduce(message.reduceDataMap)
    case message: Result => println(finalReducedMap.toString())
  }

  def aggregateInMemoryReduce(reducedMap: Map[String, Int]) {
    var count: Int = 0
    reducedMap.foreach((entry: (String, Int)) =>
      if (finalReducedMap.contains(entry._1)) {
        count = entry._2 + finalReducedMap.get(entry._1).get
        finalReducedMap += entry._1 -> count
      } else
        finalReducedMap += entry._1 -> entry._2)
  }
}
