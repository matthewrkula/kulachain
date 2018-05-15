package com.mattkula.kulachain.mining

import com.mattkula.kulachain.common.extension.sha256
import com.mattkula.kulachain.model.Block
import java.util.*

class Miner(
    difficulty: Int = 4,
    private val minerId: String = "Matt Kula"
) {

  private val prefix = "0".repeat(difficulty)
  private val random = Random()

  private var isCancelled = false

  fun mineBlock(data: String, previousHash: String): Block? {
    val timestamp = Date().time

    var nonce = random.nextLong()
    var hash = calculateHash(data, previousHash, timestamp, nonce)

    while (!hash.startsWith(prefix) && !isCancelled) {
      nonce++
      hash = calculateHash(data, previousHash, timestamp, nonce)
    }

    return if (isCancelled) {
      isCancelled = false
      null
    } else {
      Block(hash, previousHash, nonce, data, timestamp, minerId)
    }
  }

  fun cancelCurrentBlock() {
    isCancelled = true
  }

  fun verifyBlock(newBlock: Block, parentBlock: Block): Boolean {
    if (newBlock.previousHash != parentBlock.hash) {
      return false
    }

    val newHash = calculateHash(
        newBlock.data,
        parentBlock.hash,
        newBlock.timestamp,
        newBlock.nonce
    )

    return newHash.startsWith(prefix) && newHash == newBlock.hash
  }

  fun calculateHash(
      data: String,
      previousHash: String,
      timestamp: Long,
      nonce: Long
  ): String = "$previousHash$timestamp$data$nonce".sha256()
}