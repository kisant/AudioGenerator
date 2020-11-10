package com.example.audiogenerater

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_waves.*

class WavesActivity : AppCompatActivity() {
    private var FREQ_HZ = 0
    private var DUTY_CYCLE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waves)

        DUTY_CYCLE = intent.getIntExtra("duty cycle", 0)
        FREQ_HZ = intent.getIntExtra("frequency", 0)

        var buffer = shortArrayOf()

        generate.setOnClickListener {
            buffer = generateSomeWaves()
            val toast = Toast.makeText(applicationContext,
                "Signal generated successfully",
                Toast.LENGTH_SHORT)
            toast.show()
            MainActivity.generatePlayer(buffer)
        }

        findViewById<Button>(R.id.graph).setOnClickListener {
            val intent = Intent(this, GraphActivity::class.java).apply {
                putExtra("q", buffer)
            }
            startActivity(intent)
        }
    }

    private fun generateSomeWaves(): ShortArray {
        val bufferHelp = switchChecked()
        val buffer = ShortArray(bufferHelp[0]!!.size)
        var y = 0
        repeat(buffer.size) {index ->
            for (element in bufferHelp.values) {
                y += element[index]
            }
            buffer[index] = y.toShort()
            y = 0
        }
        return buffer
    }

    private fun switchChecked(): MutableMap<Int, ShortArray> {
        val signals: MutableMap<Int, ShortArray> = mutableMapOf()
        var index = 0
        if (sw_sine.isChecked) {
            signals.put(index, SignalGenerator(FREQ_HZ).sinWave())
            index += 1
        }
        if (sw_square.isChecked) {
            signals.put(index, SignalGenerator(FREQ_HZ).squareWave(DUTY_CYCLE))
            index += 1
        }
        if (sw_triangular.isChecked) {
            signals.put(index, SignalGenerator(FREQ_HZ).triangularWave())
            index += 1
        }
        if (sw_saw.isChecked) {
            signals.put(index, SignalGenerator(FREQ_HZ).sawWave())
            index += 1
        }
        if (sw_noise.isChecked) {
            signals.put(index, SignalGenerator(FREQ_HZ).noiseWave())
            index += 1
        }
        return signals
    }

    companion object {
    }
}