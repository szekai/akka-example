package sky.my.scala

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import first.actors.{Result, MasterActor}

/**
  * Created by szekai on 20/08/2014.
  */
object ClientExample1 {
   def main(args: Array[String]) {
     val _system = ActorSystem("client", ConfigFactory.load("remoteCreate"))
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
