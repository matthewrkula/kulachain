package com.mattkula.kulachain

import com.mattkula.kulachain.blockchain.Blockchain
import com.mattkula.kulachain.mining.Miner
import com.mattkula.kulachain.mining.MiningJob
import com.mattkula.kulachain.model.Block

class Main {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      Main().startChain()
    }
  }

  private val jobs = mutableListOf<MiningJob>()

  fun startChain() {
    val miner = Miner(2)
    val genesis = miner.mineBlock("This is the genesis block", "0", -1)
    println("Found genesis block ${genesis!!.hash}")

    val blockchain = Blockchain(genesis)

    for (i in 1..10) {
      jobs.add(MiningJob(
          id = "Miner #$i",
          onBlockMined = this::notifyAllOfNewBlock,
          blockchainMaster = blockchain
      ))
    }

    jobs.forEach { Thread(it).start() }
  }

  private fun notifyAllOfNewBlock(block: Block) {
    jobs.forEach {
      it.onNewBlockFound(block)
    }
  }
}