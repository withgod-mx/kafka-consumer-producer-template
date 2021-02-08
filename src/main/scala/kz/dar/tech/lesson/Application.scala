package kz.dar.tech.lesson

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.{ActorSystem, Behavior, Terminated}
import akka.{NotUsed, actor => classic}
import com.typesafe.config.{Config, ConfigFactory}
import kz.dar.tech.lesson.service.AlpakkaKafkaConsumer
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext

/**
 * Created by Yerke
 */
object Application {

  val logger: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  implicit val config: Config = ConfigFactory.load()

  def apply(): Behavior[NotUsed] = Behaviors.setup { context =>
    logger.info("Start lesson test")

    implicit val classicSystem: classic.ActorSystem = context.system.toClassic

    implicit val systemActor: ActorSystem[_] = context.system

    implicit def executionContext: ExecutionContext = systemActor.executionContext

    val alpakkaKafkaConsumer = new AlpakkaKafkaConsumer()

    alpakkaKafkaConsumer.run()

    Behaviors.receiveSignal {
      case (_, Terminated(_)) =>
        Behaviors.stopped
    }
  }

  def main(args: Array[String]): Unit = {
    ActorSystem(Application(), "kafka-template")
  }

}
