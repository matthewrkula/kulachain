package com.mattkula.kulachain.mining

import com.mattkula.kulachain.LogUtil
import com.mattkula.kulachain.common.extension.sha256
import com.mattkula.kulachain.model.Block

class MiningJob(
    private val id: String,
    private val onBlockMined: (Block) -> Unit,
    blockchainMaster: MutableList<Block>
) : Runnable {

  private val miner = Miner(minerId = id)
  private val blockchain = blockchainMaster.toMutableList()

  override fun run() {
    while (shouldContinue()) {
      val nextBlock = miner.mineBlock("This is the block #${blockchain.size}", blockchain.last().hash) ?: continue
      synchronized(blockchain) {
        blockchain.add(nextBlock)

        println("$id found block ${nextBlock.hash}")
        onBlockMined(nextBlock)
      }
    }

//    println("$id: ${uniqueString()}")
    LogUtil.gsonPrint(blockchain)
  }

  fun onNewBlockFound(candidate: Block) {
    synchronized(blockchain) {
      // This job already found this block, ignore it
      val alreadyFound = blockchain.find { it.hash === candidate.hash }
      if (alreadyFound != null) {
        return
      }

      val parent = blockchain.find { it.hash === candidate.previousHash } ?: return

      if (parent !== blockchain.last()) {
        println("Parent is not the last in the chain")
        return
      }

      if (miner.verifyBlock(candidate, parent)) {
        println("$id accepts block ${candidate.hash}")
        blockchain.add(candidate)
        miner.cancelCurrentBlock()
      } else {
        println("$id rejects block")
      }
    }
  }

  private fun shouldContinue(): Boolean = blockchain.size < 10
  private fun uniqueString(): String = blockchain.fold("", { acc, block -> acc + block.hash }).sha256()
}