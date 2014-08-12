package sky.my.scala
import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props}
import sky.io.Client
/**
 * @author ${user.name}
 */
object App {
  
  def main(args : Array[String]) {
    val system = ActorSystem("demo-system")
    val remote = new InetSocketAddress("localhost", 11111)
    val client = system.actorOf(Client.props[Client])
  }

}
