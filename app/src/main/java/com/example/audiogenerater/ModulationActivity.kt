package com.example.audiogenerater

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_modulation.*


class ModulationActivity : AppCompatActivity() {
    private var FREQ_CARRIER = 0
    private var FREQ_MODELING = 0
    private var DUTY_CYCLE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modulation)

        DUTY_CYCLE = intent.getIntExtra("duty cycle", 0)

        bt_graph.setOnClickListener {
            val buffer = typeModeling()
            val intent = Intent(this, GraphActivity::class.java).apply {
                putExtra("q", buffer)
            }
            startActivity(intent)
        }

        sb_carrier_frequency.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sb_carrier_frequency.max = 300
                tv_carrier_frequency.text = progress.toString()
                FREQ_CARRIER = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        sb_modeling_frequency.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sb_modeling_frequency.max = 20
                tv_modeling_frequency.text = progress.toString()
                FREQ_MODELING = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        bt_generate.setOnClickListener {
            MainActivity.generatePlayer(typeModeling())
        }
    }

    private fun typeModeling(): ShortArray {
        if (rb_amplitude.isChecked)
            return amplitudeModeling()
        else (rb_frequency.isChecked)
            return frequencyModeling()
    }

    private fun amplitudeModeling(): ShortArray {
        val bufferModeling = modelingSignal()
        return carrierAmplitudeSignal(bufferModeling)
    }

    private fun frequencyModeling(): ShortArray {
        val bufferModeling = modelingSignal()
        return carrierFrequencySignal(bufferModeling)
    }

    private fun modelingSignal(): ShortArray {
        if (rb_sine_m.isChecked)
            return SignalGenerator(FREQ_MODELING).sinWave()
        if (rb_square_m.isChecked)
            return SignalGenerator(FREQ_MODELING).squareWave(DUTY_CYCLE)
        if (rb_triangular_m.isChecked)
            return SignalGenerator(FREQ_MODELING).triangularWave()
        if (rb_saw_m.isChecked)
            return SignalGenerator(FREQ_MODELING).sawWave()
        return if (rb_noise_m.isChecked)
            SignalGenerator(FREQ_MODELING).noiseWave()
        else shortArrayOf()
    }

    private fun carrierAmplitudeSignal(bufferLFO: ShortArray): ShortArray {
        if (rb_sine_c.isChecked)
            return SignalGenerator(FREQ_CARRIER).sinWaveAmplitudeModeling(bufferLFO)
        if (rb_square_c.isChecked)
            return SignalGenerator(FREQ_CARRIER).squareWaveAmplitudeModeling(DUTY_CYCLE ,bufferLFO)
        if (rb_triangular_c.isChecked)
            return SignalGenerator(FREQ_CARRIER).triangularWaveAmplitudeModeling(bufferLFO)
        if (rb_saw_c.isChecked)
            return SignalGenerator(FREQ_CARRIER).sawWaveAmplitudeModeling(bufferLFO)
        return if (rb_noise_c.isChecked)
            SignalGenerator(FREQ_CARRIER).noiseWaveAmplitudeModeling(bufferLFO)
        else shortArrayOf()
    }

    private fun carrierFrequencySignal(bufferModeling: ShortArray): ShortArray {
        if (rb_sine_c.isChecked)
            return SignalGenerator(FREQ_CARRIER).sinWaveFrequencyModeling(bufferModeling)
        if (rb_square_c.isChecked)
            return SignalGenerator(FREQ_CARRIER).squareWaveFrequencyModeling(DUTY_CYCLE, bufferModeling)
        if (rb_triangular_c.isChecked)
            return SignalGenerator(FREQ_CARRIER).triangularWaveFrequencyModeling(bufferModeling)
        if (rb_saw_c.isChecked)
            return SignalGenerator(FREQ_CARRIER).sawWaveFrequencyModeling(bufferModeling)
        return if (rb_noise_c.isChecked)
            SignalGenerator(FREQ_CARRIER).noiseWave()
        else shortArrayOf()
    }
}