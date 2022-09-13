package com.example.jetpackwavesynth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.math.exp
import kotlin.math.ln

class WavetableSynthesizerViewModel : ViewModel() {


    var wavetableSynthesizer: WavetableSynthesizer? = null
        set(value) {
            field = value
            applyParameters()
        }

    private val _frequency = MutableLiveData(300f)
    val frequency: LiveData<Float>
        get() {
            return _frequency
        }


    /**
     * @param frequencySliderPosition slider position in [0, 1] range
     */
    fun setFrequencySliderPosition(frequencySliderPosition: Float) {
        val frequencyInHz = frequencyInHzFromSliderPosition(frequencySliderPosition)
        // sets the value of the MutableLiveData instance that holds the UI frequency value.
        // //This causes the UI to recompose and display the new value.
        _frequency.value = frequencyInHz

        viewModelScope.launch {
            wavetableSynthesizer?.setFrequency(frequencyInHz)
        }
    }


    // The range of generated frequencies
    private val frequencyRange = 40f..3000f

    private fun frequencyInHzFromSliderPosition(sliderPosition: Float): Float {
        val rangePosition = linearToExponential(sliderPosition)
        return valueFromRangePosition(frequencyRange, rangePosition)
    }


    fun sliderPositionFromFrequencyInHz(frequencyInHz: Float): Float {
        val rangePosition = rangePositionFromValue(frequencyRange, frequencyInHz)
        return exponentialToLinear(rangePosition)
    }

    private val _volume = MutableLiveData(-24f)
    val volume: LiveData<Float>
        get() {
            return _volume
        }
    val volumeRange = (-60f)..0f

    fun setVolume(volumeInDb: Float) {
        _volume.value = volumeInDb
        viewModelScope.launch {
            wavetableSynthesizer?.setVolume(volumeInDb)
        }
    }

    private var wavetable = Wavetable.SINE
    fun setWavetable(newWavetable: Wavetable) {
        wavetable = newWavetable
        viewModelScope.launch {
            wavetableSynthesizer?.setWavetable(newWavetable)
        }
    }


    private val _playButtonLabel = MutableLiveData(R.string.play)
    val playButtonLabel: LiveData<Int>
        get() {
            return _playButtonLabel
        }

    fun playClicked() {
        // play() and stop() are suspended functions => we must launch a coroutine
        viewModelScope.launch {
            if (wavetableSynthesizer?.isPlaying() == true) {
                wavetableSynthesizer?.stop()
            } else {
                wavetableSynthesizer?.play()
            }
            // Only when the synthesizer changed its state, update the button label.
            updatePlayButtonLabel()
        }
    }

    private fun updatePlayButtonLabel() {
        viewModelScope.launch {
            if (wavetableSynthesizer?.isPlaying() == true) {
                _playButtonLabel.value = R.string.stop
            } else {
                _playButtonLabel.value = R.string.play
            }
        }
    }


    fun applyParameters() {
        viewModelScope.launch{
            wavetableSynthesizer?.setFrequency(frequency.value!!) //!! Operator assures not null
            wavetableSynthesizer?.setVolume(volume.value!!)
            wavetableSynthesizer?.setWavetable(wavetable)
            updatePlayButtonLabel()
        }
    }



    // Companion object for conversion similar to static  java class
    companion object LinearToExponentialConverter {

        private const val MINIMUM_VALUE = 0.001f
        fun linearToExponential(value: Float): Float {
            assert(value in 0f..1f)

            if (value < MINIMUM_VALUE) {
                return 0f
            }

            return exp(ln(MINIMUM_VALUE) - ln(MINIMUM_VALUE) * value)
        }

        fun valueFromRangePosition(
            range: ClosedFloatingPointRange<Float>,
            rangePosition: Float
        ) =
            range.start + (range.endInclusive - range.start) * rangePosition

        fun rangePositionFromValue(
            range: ClosedFloatingPointRange<Float>,
            value: Float
        ): Float {
            assert(value in range)

            return (value - range.start) / (range.endInclusive - range.start)
        }

        fun exponentialToLinear(rangePosition: Float): Float {
            assert(rangePosition in 0f..1f)

            if (rangePosition < MINIMUM_VALUE) {
                return rangePosition
            }

            return (ln(rangePosition) - ln(MINIMUM_VALUE)) / (-ln(MINIMUM_VALUE))
        }
    }


}