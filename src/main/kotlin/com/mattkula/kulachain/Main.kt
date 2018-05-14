package com.mattkula.kulachain

import com.google.gson.Gson
import com.mattkula.kulachain.mining.Miner

class Main {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val gson = Gson().newBuilder().setPrettyPrinting().create()

      val miner = Miner()

      val genesis = miner.mineBlock("This is the genesis block", "0")
      val blockchain = mutableListOf(genesis)

      for (i in 1..100) {
        val nextBlock = miner.mineBlock("This is the block # ${blockchain.size}", blockchain.last().hash)

        println("Mined a new block")
        println("===========================")
        println(gson.toJson(nextBlock))

        blockchain.add(nextBlock)
      }

//      print(gson.toJson(blockchain))
    }
  }
}