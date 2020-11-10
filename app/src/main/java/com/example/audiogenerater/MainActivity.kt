package com.example.audiogenerater

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var FREQ_HZ = 0
    var DUTY_CYCLE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<SeekBar>(R.id.seekBar).setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                progressBar.progress = progress
                DUTY_CYCLE = progress
                textView.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        findViewById<SeekBar>(R.id.sb_frequency).setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                seekBar.max = 600
                pb_frequency.max = 600
                pb_frequency.progress = progress
                FREQ_HZ = progress
                tv_frequency.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        findViewById<Button>(R.id.graph).setOnClickListener {
            val b = SignalGenerator(FREQ_HZ).sinWave()
            val intent = Intent(this, GraphActivity::class.java).apply {
                putExtra("q", SignalGenerator(FREQ_HZ).squareWave(DUTY_CYCLE))
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.sine).setOnClickListener {
            generatePlayer(SignalGenerator(FREQ_HZ).sinWave())
        }

        findViewById<Button>(R.id.square).setOnClickListener {
            generatePlayer(SignalGenerator(FREQ_HZ).squareWave(DUTY_CYCLE))
        }

        findViewById<Button>(R.id.triangular).setOnClickListener {
            generatePlayer(SignalGenerator(FREQ_HZ).triangularWave())
        }

        findViewById<Button>(R.id.saw).setOnClickListener {
            generatePlayer(SignalGenerator(FREQ_HZ).sawWave())
        }

        findViewById<Button>(R.id.noise).setOnClickListener {
            generatePlayer(SignalGenerator(FREQ_HZ).noiseWave())
        }

        findViewById<Button>(R.id.waves).setOnClickListener {
            val intent = Intent(this, WavesActivity::class.java).apply {
                putExtra("duty cycle", DUTY_CYCLE)
                putExtra("frequency", FREQ_HZ)
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.modulation).setOnClickListener {
            val intent = Intent(this, ModulationActivity::class.java).apply {
                putExtra("duty cycle", DUTY_CYCLE)
                putExtra("frequency", FREQ_HZ)
            }
            startActivity(intent)
        }
    }

    companion object {
        fun generatePlayer(buffer: ShortArray) {
            val player = AudioTrack.Builder()
                    .setAudioAttributes(AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())
                    .setAudioFormat(AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(44100)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                        .build())
                    .setBufferSizeInBytes(buffer.size)
                    .build()
            player.write(buffer, 0, buffer.size)
            player.play()
        }
    }
}