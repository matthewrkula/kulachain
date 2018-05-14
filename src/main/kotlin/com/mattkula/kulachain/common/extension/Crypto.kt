package com.mattkula.kulachain.common.extension

import java.security.MessageDigest

fun String.sha256(): String {
  val bytes = toByteArray()
  val md = MessageDigest.getInstance("SHA-256")
  return md.digest(bytes).fold("", { str, it -> str + "%02x".format(it) })
}