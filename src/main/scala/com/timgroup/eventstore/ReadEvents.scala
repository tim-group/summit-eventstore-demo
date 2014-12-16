package com.timgroup.eventstore

import java.net.InetSocketAddress

import akka.actor.Status.Failure
import akka.actor._
import eventstore._
import eventstore.tcp.ConnectionActor
object ReadEvents extends App {


  val system = ActorSystem()

  val settings = Settings(
    address = new InetSocketAddress("172.16.1.71", 1113))

  val connection = system.actorOf(ConnectionActor.props(settings))
  implicit val readResult = system.actorOf(Props[ReadResult])

  system.actorOf(StreamSubscriptionActor.props(connection, readResult, EventStream.Id("fx-spot-rates"), Some(EventNumber.First)))

  class ReadResult extends Actor with ActorLogging {
    def receive = {
      case ReadEventCompleted(event) =>
        println(event)
        log.info("event: {}", event)
        context.system.shutdown()

      case Failure(e: EsException) =>
        println(e.getMessage)
        log.error(e.toString)
        context.system.shutdown()
      case ReadStreamEventsCompleted(events, _,_,_,_,_ ) => {
        events.foreach(e => println(e.data))
        context.system.shutdown()
      }
      case d @ _ => println(d)
    }
  }

}
