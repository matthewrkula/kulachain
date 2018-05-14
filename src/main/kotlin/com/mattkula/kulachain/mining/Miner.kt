package com.mattkula.kulachain.mining

import com.mattkula.kulachain.common.extension.sha256
import com.mattkula.kulachain.model.Block
import java.util.*

class Miner(difficulty: Int = 4) {

  private val prefix = "0".repeat(difficulty)

  fun mineBlock(data: String, previousHash: String): Block {
    val timestamp = Date().time

    var nonce = 0L
    var hash = calculateHash(data, previousHash, timestamp, nonce)

    while (!hash.startsWith(prefix)) {
      nonce++
      hash = calculateHash(data, previousHash, timestamp, nonce)
    }

    return Block(hash, previousHash, nonce, data)
  }

  private fun calculateHash(
      data: String,
      previousHash: String,
      timestamp: Long,
      nonce: Long
  ): String = "$previousHash$timestamp$data$nonce".sha256()
}