package com.example.audiogenerater

import android.widget.ProgressBar
import java.lang.Exception
import kotlin.math.*
import kotlin.random.Random

class SignalGenerator(frequency: Int) {
    private var FREQ_HZ = frequency
    private val SAMPLE_RATE = 44100
    private var PLAYING_TIME = SAMPLE_RATE * 5
    private var AMPLITUDE = 1

    fun sinWave(): ShortArray {
        val buffer = ShortArray(PLAYING_TIME)
        repeat(PLAYING_TIME) { index ->
            buffer[index] =
                ((AMPLITUDE * sin(2 * Math.PI * FREQ_HZ * index / SAMPLE_RATE)) * Short.MAX_VALUE)
                    .toShort()
        }
        return buffer
    }

    fun squareWave(dc: Int): ShortArray {
        val buffer = ShortArray(PLAYING_TIME)
        val dutyCycle = dc.toFloat() / 100.0
        var phi = 0.0
        repeat(PLAYING_TIME) { index ->
            phi += 2 * PI * FREQ_HZ / SAMPLE_RATE
            if (phi % (2 * PI) / (2 * PI) < dutyCycle) {
                buffer[index] = (AMPLITUDE * Short.MAX_VALUE).toShort()
            } else buffer[index] = (-AMPLITUDE * Short.MAX_VALUE).toShort()
        }
        return buffer
    }

    fun noiseWave(): ShortArray {
        val buffer = ShortArray(PLAYING_TIME)
        val max = (AMPLITUDE) * Short.MAX_VALUE
        val min = (-AMPLITUDE) * Short.MAX_VALUE
        repeat(PLAYING_TIME) { index ->
            buffer[index] = (Random.nextInt((max - min).toInt()) + min.toInt())
                .toShort()
        }
        return buffer
    }

    fun triangularWave(): ShortArray {
        val buffer = ShortArray(PLAYING_TIME)
        val help1 = 2 * AMPLITUDE / PI
        val help2 = (2 * PI) / (SAMPLE_RATE / FREQ_HZ)
        repeat(PLAYING_TIME) { index ->
            buffer[index] = ((help1 * asin(sin(help2 * index))) * Short.MAX_VALUE).toShort()
        }
        return buffer
    }

    fun sawWave(): ShortArray {
        val buffer = ShortArray(PLAYING_TIME)
        val help1 = -(2 * AMPLITUDE) / PI
        val help2 = PI / (SAMPLE_RATE / FREQ_HZ)
        repeat(PLAYING_TIME) { index ->
            buffer[index] = ((help1 * atan(1.0 / tan(index * help2))) * Short.MAX_VALUE).toShort()
        }
        return buffer
    }

    fun sinWaveAmplitudeModeling(bufferModeling: ShortArray): ShortArray {
        val buffer = ShortArray(PLAYING_TIME)
        repeat(PLAYING_TIME) { index ->
            val yLFO = bufferModeling[index].toDouble() / Short.MAX_VALUE
            buffer[index] =
                ((AMPLITUDE + AMPLITUDE * yLFO) * sin(2 * Math.PI * FREQ_HZ * index / SAMPLE_RATE)
                        * Short.MAX_VALUE)
                    .toShort()
        }
        return buffer
    }

    fun squareWaveAmplitudeModeling(dc: Int, bufferModeling: ShortArray): ShortArray {
        val buffer = ShortArray(PLAYING_TIME)
        val dutyCycle = dc.toFloat() / 100.0
        var phi = 0.0
        repeat(PLAYING_TIME) { index ->
            phi += 2 * PI * FREQ_HZ / SAMPLE_RATE
            val yLFO = bufferModeling[index].toDouble() / Short.MAX_VALUE
            if (phi % (2 * PI) / (2 * PI) < dutyCycle) {
                buffer[index] = ((AMPLITUDE + AMPLITUDE * yLFO) * Short.MAX_VALUE).toShort()
            } else
                buffer[index] = (-(AMPLITUDE + AMPLITUDE * yLFO) * Short.MAX_VALUE).toShort()
        }
        return buffer
    }

    fun noiseWaveAmplitudeModeling(bufferModeling: ShortArray): ShortArray {
        val buffer = ShortArray(PLAYING_TIME)
        repeat(PLAYING_TIME) { index ->
            val yLFO: Double = bufferModeling[index].toDouble() / Short.MAX_VALUE
            val max = (AMPLITUDE + AMPLITUDE * yLFO) * Short.MAX_VALUE
            val min = -(AMPLITUDE + AMPLITUDE * yLFO) * Short.MAX_VALUE
            buffer[index] = (Random.nextInt((max - min).toInt()) + min.toInt())
                .toShort()
        }
        return buffer
    }

    fun triangularWaveAmplitudeModeling(bufferModeling: ShortArray): ShortArray {
        val buffer = ShortArray(PLAYING_TIME)
        val help2 = (2 * PI) / (SAMPLE_RATE / FREQ_HZ)
        repeat(PLAYING_TIME) { index ->
            val yLFO = bufferModeling[index].toDouble() / Short.MAX_VALUE
            buffer[index] = ((2 * (AMPLITUDE + AMPLITUDE * yLFO) / PI
                    * asin(sin(help2 * index))) * Short.MAX_VALUE).toShort()
        }
        return buffer
    }

    fun sawWaveAmplitudeModeling(bufferModeling: ShortArray): ShortArray {
        val buffer = ShortArray(PLAYING_TIME)
        val help2 = PI / (SAMPLE_RATE / FREQ_HZ)
        repeat(PLAYING_TIME) { index ->
            val yLFO = bufferModeling[index].toDouble() / Short.MAX_VALUE
            buffer[index] = ((-(2 * (AMPLITUDE + AMPLITUDE * yLFO)) / PI
                    * atan(1.0 / tan(index * help2))) * Short.MAX_VALUE).toShort()
        }
        return buffer
    }

    fun sinWaveFrequencyModeling(bufferModeling: ShortArray): ShortArray {
        val buffer = ShortArray(PLAYING_TIME)
        var phi = 0.0
        repeat(PLAYING_TIME) { index ->
            val yLFO = bufferModeling[index].toDouble() / Short.MAX_VALUE
            phi += 2 * PI * (FREQ_HZ + FREQ_HZ * yLFO) / SAMPLE_RATE
            buffer[index] =
                ((AMPLITUDE * sin(phi)) * Short.MAX_VALUE)
                    .toShort()
        }
        return buffer
    }

    fun squareWaveFrequencyModeling(dc: Int, bufferModeling: ShortArray): ShortArray {
        val buffer = ShortArray(PLAYING_TIME)
        val dutyCycle = dc.toFloat() / 100.0
        var phi = 0.0
        repeat(PLAYING_TIME) { index ->
            val yLFO: Double = bufferModeling[index].toDouble() / Short.MAX_VALUE
            val temp = 2 * PI * (FREQ_HZ + FREQ_HZ * yLFO) / SAMPLE_RATE
            phi += temp
            if (phi % (2 * PI) / (2 * PI) < dutyCycle) {
                buffer[index] = (AMPLITUDE * Short.MAX_VALUE).toShort()
            } else buffer[index] = (-AMPLITUDE * Short.MAX_VALUE).toShort()
        }
        return buffer
    }

    fun triangularWaveFrequencyModeling(bufferModeling: ShortArray): ShortArray {
        val buffer = ShortArray(PLAYING_TIME)
        val help1 = 2 * AMPLITUDE / PI
        var phi = 0.0
        repeat(PLAYING_TIME) { index ->
            val yLFO = bufferModeling[index].toDouble() / Short.MAX_VALUE
            phi += (2 * PI) * (FREQ_HZ + FREQ_HZ * yLFO) / (SAMPLE_RATE )
            buffer[index] = ((help1 * asin(sin(phi))) * Short.MAX_VALUE).toShort()
        }
        return buffer
    }

    fun sawWaveFrequencyModeling(bufferModeling: ShortArray): ShortArray {
        val buffer = ShortArray(PLAYING_TIME)
        val help1 = -(2 * AMPLITUDE) / PI
        var phi = 0.0
        repeat(PLAYING_TIME) { index ->
            val yLFO = bufferModeling[index].toDouble() / Short.MAX_VALUE
            phi += PI * (FREQ_HZ + FREQ_HZ * yLFO) / (SAMPLE_RATE)
            buffer[index] = ((help1 * atan(1.0 / tan(phi))) * Short.MAX_VALUE).toShort()
        }
        return buffer
    }
}