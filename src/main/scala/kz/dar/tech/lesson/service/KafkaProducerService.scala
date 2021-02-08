package kz.dar.tech.lesson.service
import java.util.Properties

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization.StringSerializer

class KafkaProducerService {

  implicit val config: Config = ConfigFactory.load()

  private val KAFKA_TOPIC = "lesson-topic"

  val producer = new KafkaProducer[String, String](configuration)

  private def configuration: Properties = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props
  }

  def sendMessage(value: String) = {
    val record = new ProducerRecord[String, String](KAFKA_TOPIC, "key", value)
    producer.send(record)
    producer.close()
  }

}

object KafkaProducerService {
  val producer = new KafkaProducerService()

  def send(msg: String) = {
    producer.sendMessage(msg)
  }

}
