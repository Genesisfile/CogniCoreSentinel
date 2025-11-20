package com.sentinel.cognicore

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

object HyperOptimalDispatcher {
    private val _spotPrice = MutableStateFlow(100)
    val spotPrice: StateFlow<Int> = _spotPrice

    fun simulateMarket() {
        val fluctuation = Random.nextInt(-10, 15)
        val current = _spotPrice.value
        _spotPrice.value = (current + fluctuation).coerceIn(50, 500)
    }

    fun bidForExecution(priority: Int): Boolean {
        // High priority bids always win, others depend on spot price
        return if (priority == 0) true else _spotPrice.value < 300
    }
}
