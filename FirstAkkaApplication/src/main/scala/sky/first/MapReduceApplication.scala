package sky.first

import akka.actor.{Props, ActorSystem}
import sky.first.actors.MasterActor
import sky.first.actors.Result

/**
 * Created by szekai on 20/08/2014.
 */
object MapReduceApplication {
  def main(args: Array[String]) {
    val _system = ActorSystem("MapReduceApp")
    val master = _system.actorOf(Props[MasterActor], name = "master")

    master ! "The quick brown fox tried to jump over the lazy dog and fell on the dog"
    master ! "Dog is man's best friend"
    master ! "Dog and Fox belong to the same family"

    Thread.sleep(500)
    master ! new Result

    Thread.sleep(500)
    _system.shutdown
    println("Scala done!")
  }
}
