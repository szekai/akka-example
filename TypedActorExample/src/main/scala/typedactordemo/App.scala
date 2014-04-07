package typedactordemo

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

case class Request(payload: String)
case class Response(payload: String)

trait Service {
  def request(r: Request): Future[Response]
}

class ServiceImpl extends Service {
  val actor = {
    val ctx = TypedActor.context
    ctx.actorOf(Props[ServiceActor])
  }

  implicit val timeout = Timeout(10 seconds)

  def request(req: Request): Future[Response] =
    (actor ? req).mapTo[Response]
}

class ServiceActor extends Actor {
  def receive = {
    case Request(payload) =>
      sender ! Response(payload)
  }
}

object App {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("TypedActorDemo")

    val service: Service =
      TypedActor(system).typedActorOf(
        TypedProps[ServiceImpl]())

    val req = Request("hello world!")

    service.request(req) onSuccess {
      case Response(response) =>
        println(response)
        system.shutdown()
    }
  }
}
