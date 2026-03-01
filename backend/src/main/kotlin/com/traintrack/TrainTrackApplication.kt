package com.traintrack

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class TrainTrackApplication

fun main(args: Array<String>) {
    runApplication<TrainTrackApplication>(*args)
}
