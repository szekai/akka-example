package sky.my.scala

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

/**
 * @author ${user.name}
 */
object App {
  
  def main(args : Array[String]) {
    ActorSystem("mapredSystem", ConfigFactory.load("remote"))
  }

}
