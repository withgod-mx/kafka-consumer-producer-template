package kz.dar.tech.lesson.service

import akka.Done
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter.TypedActorSystemOps
import akka.event.slf4j.Logger
import akka.kafka.scaladsl.{Committer, Consumer}
import akka.kafka.{CommitterSettings, ConsumerMessage, ConsumerSettings, Subscriptions}
import akka.stream.scaladsl.{Keep, RestartSource, Sink}
import akka.util.Timeout
import kz.dar.tech.lesson.util.CustomConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

/**
 * Alpakka example
 * Created by Yerke
 * @param systemActor
 */
class AlpakkaKafkaConsumer()(implicit systemActor: ActorSystem[_]) {

  private val logger = Logger(getClass.getSimpleName)
  implicit val executeContext = systemActor.executionContext
  implicit val timeout: Timeout = 3.seconds


  val committerSettings = CommitterSettings(systemActor.toClassic)


  val consumerSettings = ConsumerSettings(systemActor.settings.config.getConfig("akka.kafka.consumer"), new StringDeserializer, new StringDeserializer)
    .withBootstrapServers(CustomConfig.Consuming.BOOTSTRAP_SERVERS_LIST)
    .withGroupId(CustomConfig.Consuming.TRANSPORT_TOPICS_GROUP)
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")


  def messageHandler(msg: ConsumerMessage.CommittableMessage[String, String]): Future[Done] = {
    logger.info(s"Consuming message from topic ${msg.record.topic()}; value: ${msg.record.value()}")

    msg.record.topic() match {
      case CustomConfig.Consuming.LESSON_TOPIC => {
        println(msg.record.value)
        Future(Done)
      }
    }
  }


  def run() = {
    RestartSource.onFailuresWithBackoff(minBackoff = 3.seconds, maxBackoff = 30.seconds, randomFactor = 0.2) {
      () =>
        Consumer.committableSource(
          consumerSettings,
          Subscriptions.topics(
            CustomConfig.Consuming.LESSON_TOPIC
          )
        )
          .mapAsync(1) {
            msg =>
              messageHandler(msg).map(_ => msg.committableOffset)
          }.via(Committer.flow(committerSettings.withMaxBatch(1)))
    }.toMat(Sink.seq)(Keep.both)
      .run()
  }
}

