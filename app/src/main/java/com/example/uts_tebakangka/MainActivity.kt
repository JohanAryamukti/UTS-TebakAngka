package com.example.uts_tebakangka

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var targetNumber = 0
    private var score = 100
    private var minRange = 0
    private var maxRange = 100
    private val historyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvScore = findViewById<TextView>(R.id.tvScore)
        val tvRange = findViewById<TextView>(R.id.tvRange)
        val etGuess = findViewById<EditText>(R.id.etGuess)
        val btnGuess = findViewById<Button>(R.id.btnGuess)
        val tvFeedback = findViewById<TextView>(R.id.tvFeedback)
        val btnReset = findViewById<Button>(R.id.btnReset)
        val tvHistory = findViewById<TextView>(R.id.tvHistory)

        fun resetGame() {
            targetNumber = Random.nextInt(0, 101)
            score = 100
            minRange = 0
            maxRange = 100
            historyList.clear()

            tvScore.text = "Skor: $score"
            tvRange.text = "$minRange  —  $maxRange"
            tvFeedback.text = "Masukkan angka dari 0 sampai 100"
            tvFeedback.setTextColor(Color.parseColor("#6366F1")) // Indigo
            tvHistory.text = "Belum ada tebakan."
            etGuess.text.clear()
            etGuess.isEnabled = true
            btnGuess.isEnabled = true
            btnReset.visibility = View.GONE
        }

        resetGame()

        btnGuess.setOnClickListener {
            val input = etGuess.text.toString()
            val userGuess = input.toIntOrNull()

            if (userGuess == null) {
                tvFeedback.text = "Masukkan angka yang valid!"
                tvFeedback.setTextColor(Color.parseColor("#EF4444")) // Red Crimson
                return@setOnClickListener
            }

            if (userGuess !in minRange..maxRange) {
                tvFeedback.text = "Angka harus di dalam rentang $minRange - $maxRange!"
                tvFeedback.setTextColor(Color.parseColor("#EF4444")) // Red Crimson
                return@setOnClickListener
            }

            if (userGuess == targetNumber) {
                tvFeedback.text = "Selamat! Angka simpanan adalah $targetNumber!"
                tvFeedback.setTextColor(Color.parseColor("#10B981")) // Emerald Green
                historyList.add(0, "Tebakan $userGuess: BENAR (Skor: $score)")
                btnGuess.isEnabled = false
                etGuess.isEnabled = false
                btnReset.visibility = View.VISIBLE
            } else {
                score -= 10
                if (score <= 0) {
                    score = 0
                    tvFeedback.text = "Game Over! Angka simpanan adalah $targetNumber."
                    tvFeedback.setTextColor(Color.parseColor("#EF4444")) // Red Crimson
                    historyList.add(0, "Tebakan $userGuess: SALAH (Game Over)")
                    btnGuess.isEnabled = false
                    etGuess.isEnabled = false
                    btnReset.visibility = View.VISIBLE
                } else if (userGuess < targetNumber) {
                    tvFeedback.text = "Angka $userGuess terlalu kecil"
                    tvFeedback.setTextColor(Color.parseColor("#F59E0B")) // Vibrant Amber
                    minRange = maxOf(minRange, userGuess + 1)
                    historyList.add(0, "Tebakan $userGuess: Terlalu kecil")
                } else {
                    tvFeedback.text = "Angka $userGuess terlalu besar"
                    tvFeedback.setTextColor(Color.parseColor("#F59E0B")) // Vibrant Amber
                    maxRange = minOf(maxRange, userGuess - 1)
                    historyList.add(0, "Tebakan $userGuess: Terlalu besar")
                }
            }

            tvScore.text = "Skor: $score"
            tvRange.text = "$minRange  —  $maxRange"
            tvHistory.text = historyList.joinToString("\n")
            etGuess.text.clear()
        }

        btnReset.setOnClickListener {
            resetGame()
        }
    }
}