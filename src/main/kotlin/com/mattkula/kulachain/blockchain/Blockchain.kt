package com.mattkula.kulachain.blockchain

import com.mattkula.kulachain.model.Block

private class Node(val block: Block)

class Blockchain {
  private val data: HashMap<String, Node>

  constructor(genesisBlock: Block) {
    data = hashMapOf(genesisBlock.hash to Node(genesisBlock))
  }

  // Copy constructor
  private constructor(initialData: HashMap<String, Node>) {
    data = HashMap(initialData)
  }

  fun addBlock(block: Block) {
    data[block.previousHash] ?: return

    data[block.hash] = Node(block)
  }

  fun getBlock(hash: String): Block? = data[hash]?.block

  fun getDeepestBlock(): Block = data.maxBy { it.value.block.depth }!!.value.block

  fun copy(): Blockchain = Blockchain(data)

  fun longestList(): List<Block> {
    var block: Block? = getDeepestBlock()

    val list = mutableListOf<Block>()

    while (block != null) {
      list.add(block)
      block = data[block.previousHash]?.block
    }

    list.reverse()
    return list
  }
}