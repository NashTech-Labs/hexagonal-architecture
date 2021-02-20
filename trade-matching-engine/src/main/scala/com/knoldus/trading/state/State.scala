package com.knoldus.trading.state

trait State {
  def isFinalState: Boolean

  final def isNonFinalState: Boolean = !isFinalState
}

trait FinalState {
  def isFinalState: Boolean = true
}

trait NonFinalState {
  def isFinalState: Boolean = false
}
