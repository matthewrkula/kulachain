package com.mattkula.kulachain.mining

import com.mattkula.kulachain.LogUtil
import com.mattkula.kulachain.blockchain.Blockchain
import com.mattkula.kulachain.common.extension.sha256
import com.mattkula.kulachain.model.Block

class MiningJob(
    private val id: String,
    private val onBlockMined: (Block) -> Unit,
    blockchainMaster: Blockchain
) : Runnable {

  private val miner = Miner(minerId = id)
  private val blockchain = blockchainMaster.copy()

  override fun run() {
    while (shouldContinue()) {
      val deepestBlock = blockchain.getDeepestBlock()
      val nextBlock = miner.mineBlock("This is some data", deepestBlock.hash, deepestBlock.depth) ?: continue
      synchronized(blockchain) {
        blockchain.addBlock(nextBlock)

        println("$id found block ${nextBlock.hash}")
        onBlockMined(nextBlock)
      }
    }

    println("$id: ${uniqueString()}")
    LogUtil.gsonPrint(blockchain.longestList())
  }

  fun onNewBlockFound(candidate: Block) {
    synchronized(blockchain) {
      // This job already found this block, ignore it
      val alreadyFound = blockchain.getBlock(candidate.hash)
      if (alreadyFound != null) {
        return
      }

      val parent = blockchain.getBlock(candidate.previousHash) ?: return

      if (miner.verifyBlock(candidate, parent)) {
        println("$id accepts block ${candidate.hash}")
        blockchain.addBlock(candidate)
        miner.cancelCurrentBlock()
      } else {
        println("$id rejects block")
      }
    }
  }

  private fun shouldContinue(): Boolean = blockchain.getDeepestBlock().depth < 10
  private fun uniqueString(): String = blockchain.longestList().fold("", { acc, block -> acc + block.hash }).sha256()
}