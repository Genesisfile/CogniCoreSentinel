package com.sentinel.cognicore

class BehavioralAnomalySentinel {
    fun auditTrace(trace: String): Boolean {
        // Simple heuristic: Reject traces with "overflow"
        return !trace.contains("overflow")
    }
}
