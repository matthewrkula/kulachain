package com.mattkula.kulachain.mining

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MinerTest {

  @Test
  fun testMiner_minesUntilAnswerFound() {
    val miner = Miner(3)

    val testData = "Test data"

    val block = miner.mineBlock(testData, "123")!!

    assertEquals(block.hash, miner.calculateHash(testData, "123", block.timestamp, block.nonce))
    assertTrue(block.hash.startsWith("000"))
  }
}