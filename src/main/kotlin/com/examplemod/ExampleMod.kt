package com.examplemod

import net.fabricmc.api.ClientModInitializer
import org.slf4j.LoggerFactory

object ExampleMod : ClientModInitializer {

  private val logger = LoggerFactory.getLogger(this::class.java)

  override fun onInitializeClient() {
    logger.info("Hello World!")
  }

}
