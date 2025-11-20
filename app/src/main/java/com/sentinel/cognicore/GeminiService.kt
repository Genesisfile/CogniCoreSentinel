package com.sentinel.cognicore

import android.content.Context
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class GeminiService(private val context: Context) {
    private val knowledgeBase: JSONObject by lazy { loadKnowledgeBase() }

    private fun loadKnowledgeBase(): JSONObject {
        return try {
            val input = context.assets.open("cogni_core/knowledge_base.json")
            val jsonStr = BufferedReader(InputStreamReader(input)).use { it.readText() }
            JSONObject(jsonStr)
        } catch (e: Exception) {
            JSONObject()
        }
    }

    suspend fun generateResponse(prompt: String): String {
        delay(600) // Simulate inference latency
        val lowerPrompt = prompt.lowercase()
        
        // Heuristic Matching from Knowledge Base
        val entries = knowledgeBase.optJSONArray("entries")
        if (entries != null) {
            for (i in 0 until entries.length()) {
                val entry = entries.getJSONObject(i)
                if (lowerPrompt.contains(entry.optString("keyword", "---"))) {
                    return "[HEURISTIC] " + entry.optString("response", "Data corrupted.")
                }
            }
        }

        return when {
            lowerPrompt.contains("status") -> "System Nominal. HOD Active. Thermal Load: 34%"
            lowerPrompt.contains("scan") -> "Scanning... [SECURE]. No anomalies detected in local substrate."
            lowerPrompt.contains("optimize") -> "Optimization Routine Initiated. Compressing logic graphs..."
            else -> "I am the CogniCore Sentinel. Command not recognized in autonomous mode."
        }
    }
}
