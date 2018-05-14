package com.mattkula.kulachain

import com.google.gson.Gson

object LogUtil {
  private val gson = Gson().newBuilder().setPrettyPrinting().create()

  fun <T> gsonPrint(obj: T) {
    println(gson.toJson(obj))
  }
}