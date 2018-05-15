package com.mattkula.kulachain

import com.google.gson.Gson
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

    val miner = Miner()
    val genesis = miner.mineBlock("This is the genesis block", "0", -1)
    println("Found genesis block ${genesis!!.hash}")

    val blockchain = Blockchain(genesis)

    val job = MiningJob(
        id = "First miner ever",
        onBlockMined = this::notifyAllOfNewBlock,
        blockchainMaster = blockchain
    )

    val job2 = MiningJob(
        id = "2nd miner",
        onBlockMined = this::notifyAllOfNewBlock,
        blockchainMaster = blockchain
    )

    val job3 = MiningJob(
        id = "3rc miner",
        onBlockMined = this::notifyAllOfNewBlock,
        blockchainMaster = blockchain
    )

    jobs.addAll(listOf(job, job2, job3))
    jobs.forEach{ Thread(it).start() }
  }

  private fun notifyAllOfNewBlock(block: Block) {
    jobs.forEach {
      it.onNewBlockFound(block)
    }
  }
}