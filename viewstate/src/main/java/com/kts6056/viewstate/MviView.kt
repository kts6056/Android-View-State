package com.kts6056.viewstate

interface MviView<I : ViewIntent, S : ViewState, E : ViewEffect> {
    fun setIntent(intent: I)

    fun processState(state: S)

    fun processEffect(effect: E)
}
