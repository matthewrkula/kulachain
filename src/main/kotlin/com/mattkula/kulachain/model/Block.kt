package com.mattkula.kulachain.model


data class Block(
    val hash: String,
    val previousHash: String,
    val nonce: Long,
    val data: String,
    val timestamp: Long,
    val miner: String
)