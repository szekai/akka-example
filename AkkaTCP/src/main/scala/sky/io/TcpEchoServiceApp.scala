/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sky.io
import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props, Terminated }
import akka.io.{ IO, Tcp }
import java.net.InetSocketAddress
import scala.io.StdIn
object TcpEchoServiceApp {
  def main(args: Array[String]) {
    val system = ActorSystem("demo-system")
    val endpoint = new InetSocketAddress("localhost", 11111)
    system.actorOf(Props(classOf[TcpEchoService], endpoint))

    StdIn.readLine(f"Hit ENTER to exit ...%n")
    system.shutdown()
    system.awaitTermination()
  }
}

class TcpEchoService(endpoint: InetSocketAddress) extends Actor with ActorLogging {
  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, endpoint)

  def receive = {
    case b @ Bound(localAddress) =>
    // do some logging or setup ...

    case CommandFailed(_: Bind) => context stop self

    case c @ Connected(remote, local) =>
      val handler = context.actorOf(Props(classOf[TcpEchoConnectionHandler], remote, sender))
      val connection = sender()
      connection ! Register(handler)
  }

  class TcpEchoConnectionHandler(remote: InetSocketAddress, connection: ActorRef) extends Actor with ActorLogging {

    context.watch(connection) // We want to know when the connection dies without sending a `Tcp.ConnectionClosed`
    import Tcp._
    def receive: Receive = {
      case Received(data) =>
        val text = data.utf8String.trim
        log.debug("Received '{}' from remote address {}", text, remote)
        text match {
          case "close" => context.stop(self)
          case _ => sender() ! Write(data)
        }
      case _: ConnectionClosed =>
        log.debug("Stopping, because connection for remote address {} closed", remote)
        context.stop(self)
      case Terminated(`connection`) =>
        log.debug("Stopping, because connection for remote address {} died", remote)
        context.stop(self)
    }
  }
}
