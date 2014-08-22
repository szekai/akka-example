package first.actors

import akka.actor.{Actor, ActorLogging, ActorRef}

/**
 * Created by szekai on 20/08/2014.
 */
class ReduceActor (aggregateActor: ActorRef) extends Actor with ActorLogging{
  override def preStart() {
    log.info("Starting ReduceActor instance hashcode # {}", this.hashCode())
  }
  override def postStop() {
    log.info("Stopping ReduceActor instance hashcode # {}", this.hashCode())
  }
  val defaultCount: Int = 1
  def receive: Receive = {
    case message: MapData =>
      aggregateActor ! reduce(message.dataList)
  }

  def reduce(dataList: List[(String, Int)]): ReduceData = {
    def count(dataList:List[(String, Int)], ans: List[(String, Int)]):List[(String, Int)] = {
      if(dataList.isEmpty) ans
      else {
        val key = dataList.head._1
        val first = dataList.filter(p => p._1 == key)
        val value = first.foldLeft(0)((a,b) => a + b._2)
        count(dataList.filter(_._1 != key), (key->value)::ans)
      }
    }
    val reducedMap = count(dataList, List[(String, Int)]())
    return new ReduceData(reducedMap.toMap)
  }
}