package com.example.jetpackwavesynth

import android.util.Log

//LoggingWavetableSynthesizer that implements the WavetableSynthesizer interface and logs the function of the called method along with the passed-in parameters.

class LoggingWavetableSynthesizer : WavetableSynthesizer {

    private var isPlaying = false

    override suspend fun play() {
        Log.d("wavesynth", "play() called.")
        isPlaying = true
    }

    override suspend fun stop() {
        Log.d("wavesynth", "stop() called.")
        isPlaying = false
    }

    override suspend fun isPlaying(): Boolean {
        return isPlaying
    }

    override suspend fun setFrequency(frequencyInHz: Float) {
        Log.d("wavesynth", "Frequency set to $frequencyInHz Hz.")
    }

    override suspend fun setVolume(volumeInDb: Float) {
        Log.d("wavesynth", "Volume set to $volumeInDb dB.")
    }

    override suspend fun setWavetable(wavetable: Wavetable) {
        Log.d("wavesynth", "Wavetable set to $wavetable")
    }
}