package sky.supervisor.example3

import akka.actor.{Terminated, ActorRef, ActorLogging, Actor}

import scala.collection.immutable.HashMap

/**
 * Created by szekai on 19/08/2014.
 */
class MonitorActor extends Actor with ActorLogging {

  var monitoredActors = new HashMap[ActorRef, ActorRef]

  def receive: Receive = {
    case t: Terminated =>
      if (monitoredActors.contains(t.actor)) {
        log.info("Received Worker Actor Termination Message -> "
          + t.actor.path)
        log.info("Sending message to Supervisor")
        val value: Option[ActorRef] = monitoredActors.get(t.actor)
        value.get ! new DeadWorker()
      }

    case msg: RegisterWorker =>
      context.watch(msg.worker)
      monitoredActors += msg.worker -> msg.supervisor
  }
}