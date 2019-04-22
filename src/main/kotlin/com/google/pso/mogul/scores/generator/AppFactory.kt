package com.google.pso.mogul.scores.generator

import com.google.cloud.pubsub.v1.Publisher
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.pubsub.v1.ProjectTopicName
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Property
import org.apache.commons.lang3.time.FastDateFormat
import org.apache.commons.math3.distribution.EnumeratedDistribution
import org.apache.commons.math3.random.RandomDataGenerator
import org.apache.commons.math3.util.Pair
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Singleton

@Factory
internal class AppFactory(
    @Property(name = "app.gcp-project-id") val gcpProjectId: String,
    @Property(name = "app.pubsub-topic") val pubsubTopic: String
) {

  companion object {
    val logger = LoggerFactory.getLogger(AppFactory::class.java)
  }

  @Singleton
  fun players(): EnumeratedDistribution<String> {
    return EnumeratedDistribution<String>(
        Arrays.asList(
            Pair.create("player1", 0.2),
            Pair.create("player2", 0.2),
            Pair.create("player3", 0.2),
            Pair.create("player4", 0.2),
            Pair.create("player5", 0.2)
        )
    )
  }

  @Singleton
  fun scores(): RandomDataGenerator {
    return RandomDataGenerator()
  }

  @Singleton
  fun publisher(): Publisher {
    val topicName = ProjectTopicName.of(gcpProjectId, pubsubTopic)
    val publisher = Publisher.newBuilder(topicName).build()

    logger.info("Topic Name: " + publisher.topicNameString)

    return publisher
  }

  @Singleton
  fun gson(): Gson {
    return GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create()
  }

  @Singleton
  fun pubsubDateFormat(): FastDateFormat {
    return FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", TimeZone.getTimeZone("UTC"))
  }

}