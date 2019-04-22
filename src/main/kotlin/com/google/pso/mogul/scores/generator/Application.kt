package com.google.pso.mogul.scores.generator

import io.micronaut.runtime.Micronaut

object Application {

  @JvmStatic
  fun main(args: Array<String>) {
    Micronaut.build()
        .packages("com.google.pso.mogul")
        .mainClass(Application.javaClass)
        .start()
  }

}