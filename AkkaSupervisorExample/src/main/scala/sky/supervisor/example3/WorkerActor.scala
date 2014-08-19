package sky.supervisor.example3

import akka.actor.{ActorLogging, Actor}

/**
 * Created by szekai on 19/08/2014.
 */
class WorkerActor extends Actor with ActorLogging {
  var state: Int = 0

  override def preStart() {
    log.info("Starting WorkerActor instance hashcode # {}", this.hashCode())
  }
  override def postStop() {
    log.info("Stopping WorkerActor instance hashcode # {}", this.hashCode());
  }
  def receive: Receive = {
    case value: Int =>  state = value
    case result: Result =>  sender ! state
    case _ =>  context.stop(self)
  }
}
