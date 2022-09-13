package com.example.jetpackwavesynth

interface WavetableSynthesizer {
    // Function could be costly performance wise in the future ->
    // suspend functions need to be executed in a CoroutineScope.
    suspend fun play()
    suspend fun stop()
    suspend fun isPlaying() : Boolean
    suspend fun setFrequency(frequencyInHz: Float)
    suspend fun setVolume(volumeInDb: Float)
    suspend fun setWavetable(wavetable: Wavetable)
}
