package com.google.pso.mogul.scores.generator

import com.google.cloud.pubsub.v1.Publisher
import com.google.gson.Gson
import com.google.protobuf.ByteString
import com.google.pso.mogul.scores.generator.model.ScoreEvent
import com.google.pubsub.v1.PubsubMessage
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import org.apache.commons.lang3.time.FastDateFormat
import org.apache.commons.math3.distribution.EnumeratedDistribution
import org.apache.commons.math3.random.RandomDataGenerator
import java.util.*
import javax.inject.Inject


@Controller("/scores")
class ScoresController @Inject
constructor(
    private val players: EnumeratedDistribution<String>,
    private val scores: RandomDataGenerator,
    private val publisher: Publisher,
    private val gson: Gson,
    private val pubsubDateFormat: FastDateFormat) {

  @Get("/")
  @Produces(MediaType.APPLICATION_JSON)
  fun index(): ScoreEvent {

    val scoreEvent = ScoreEvent(Date(), players.sample(), scores.nextInt(0, 10))

    publisher.publish(
        PubsubMessage.newBuilder()
            .setData(ByteString.copyFromUtf8(gson.toJson(scoreEvent)))
            .putAttributes("ts_value", pubsubDateFormat.format(scoreEvent.timestamp))
            .build())

    return scoreEvent
  }

}