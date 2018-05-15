package com.mattkula.kulachain.blockchain

import com.mattkula.kulachain.model.Block
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BlockchainTest {

  private val block1 = Block("abc", "0", 0, "This is the genesis", 0, "Test miner", 0)
  private val block2 = Block("def", "abc", 10, "First block", 0, "Test miner", 1)
  val block3 = Block("ghi", "def", 20, "Has sibling", 0, "Test miner", 2)
  val block4 = Block("jkl", "def", 30, "Has sibling", 0, "Test miner", 2)
  val block5 = Block("mno", "jkl", 40, "is deepest", 0, "Test miner", 3)

  lateinit var blockchain: Blockchain

  @Before
  fun setup() {
    blockchain = Blockchain(block1)
  }

  @Test
  fun testBlockchain_hasDepth0() {
    assertEquals(block1, blockchain.getDeepestBlock())
    assertEquals(0, blockchain.getDeepestBlock().depth)
  }

  @Test
  fun testBlockchain_hasDepth1() {
    blockchain.addBlock(block2)

    assertEquals(block2, blockchain.getDeepestBlock())
    assertEquals(1, blockchain.getDeepestBlock().depth)
  }

  @Test
  fun testBlockchain_hasDepth4WithSiblings() {
    blockchain.addBlock(block2)
    blockchain.addBlock(block3)
    blockchain.addBlock(block4)
    blockchain.addBlock(block5)

    assertEquals(block5, blockchain.getDeepestBlock())
    assertEquals(3, blockchain.getDeepestBlock().depth)
  }
}