package kz.dar.tech.lesson.util

import java.util

object CustomConfig extends CommonConfig {

  object Consuming {

    val BOOTSTRAP_SERVERS_LIST = config.getString("transport.connection.hosts")
    val TRANSPORT_TOPICS_GROUP = config.getString("transport.topics.group")


    val LESSON_TOPIC = config.getString("transport.topics.consuming.lesson.topic")

    // all topics
    val TOPICS = util.Arrays.asList(
      LESSON_TOPIC
    )
  }


}
