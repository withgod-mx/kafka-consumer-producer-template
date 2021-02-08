package kz.dar.tech.lesson.util

import com.typesafe.config.ConfigFactory

trait CommonConfig {
  implicit val config = ConfigFactory.load()
}
