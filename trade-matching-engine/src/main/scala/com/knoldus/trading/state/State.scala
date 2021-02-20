package com.knoldus.trading.state

trait State {
  def isFinal: Boolean

  final def isNonFinal: Boolean = !isFinal
}

trait Final {
  def isFinal: Boolean = true
}

trait NonFinal {
  def isFinal: Boolean = false
}
