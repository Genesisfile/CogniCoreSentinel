package com.sentinel.cognicore

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sentinel.cognicore.databinding.LayoutMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: LayoutMainBinding
    private val geminiService by lazy { GeminiService(this) }
    private val logBuffer = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        startSystemLoops()
    }

    private fun setupUI() {
        binding.btnExecute.setOnClickListener {
            val cmd = binding.etInput.text.toString()
            if (cmd.isNotBlank()) {
                log("OP: $cmd", "#00bcd4") // Cyan
                binding.etInput.text.clear()
                processCommand(cmd)
            }
        }
    }

    private fun processCommand(cmd: String) {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            
            // 1. Bid for resources
            if (!HyperOptimalDispatcher.bidForExecution(1)) {
                log("HOD: Resource Bid Rejected (Spot Price High)", "#f44336")
                binding.progressBar.visibility = View.GONE
                return@launch
            }

            // 2. Execute
            val response = withContext(Dispatchers.Default) {
                geminiService.generateResponse(cmd)
            }
            
            // 3. Audit
            if (BehavioralAnomalySentinel().auditTrace(cmd)) {
                log("CORE: $response", "#4caf50") // Green
            } else {
                log("BAS: Security Violation Detected", "#ff9800") // Orange
            }
            
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun log(msg: String, color: String) {
        val timestamp = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())
        val html = "<font color='#888'>[$timestamp]</font> <font color='$color'>$msg</font><br/>"
        logBuffer.append(html)
        
        lifecycleScope.launch(Dispatchers.Main) {
            binding.tvConsole.text = Html.fromHtml(logBuffer.toString(), Html.FROM_HTML_MODE_COMPACT)
            binding.scrollView.post { binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
        }
    }

    private fun startSystemLoops() {
        lifecycleScope.launch {
            log("SYSTEM: CogniCore Sentinel v1.0 Online", "#ffffff")
            while (true) {
                HyperOptimalDispatcher.simulateMarket()
                val price = HyperOptimalDispatcher.spotPrice.value
                binding.tvStatus.text = "SPOT PRICE: ₳$price | ${PredictiveLoadOrchestrator().predictLoad()}"
                delay(2000)
            }
        }
    }
}
