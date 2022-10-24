package com.example.jetpackwavesynth


import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.jetpackwavesynth.ui.theme.JetpackWaveSynthTheme
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thewolfsound.wavetablesynthesizer.NativeWavetableSynthesizer

class MainActivity : ComponentActivity() {
    private val synthesizer = NativeWavetableSynthesizer
    private val synthesizerViewModel: WavetableSynthesizerViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        synthesizerViewModel.wavetableSynthesizer = synthesizer
        setContent {
            val systemUiController: SystemUiController = rememberSystemUiController()
            systemUiController.isSystemBarsVisible = false // Status & Navigation bars

            JetpackWaveSynthTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // pass the ViewModel down the composables' hierarchy
                    WavetableSynthesizerApp(Modifier, synthesizerViewModel)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        synthesizerViewModel.applyParameters()
    }


}


@Composable
fun WavetableSynthesizerApp(


    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,

        ) {
        // These two composables will be shortly defined
        WavetableSelectionPanel(modifier, synthesizerViewModel)
        ControlsPanel(modifier, synthesizerViewModel)
    }
}

@Composable
private fun WavetableSelectionPanel(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 32.dp),

        ) {
        Text("WAVETABLE")
        WavetableSelectionButtons(modifier, synthesizerViewModel)
    }

}

@Composable
private fun ControlsPanel(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),

        ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 32.dp),
            ) {
                Row(
                    modifier = Modifier.padding(0.dp, 12.dp)
                ) {
                    Text("FREQUENCY")

                }

                PitchControl(modifier, synthesizerViewModel)


            }


        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 32.dp),
            ) {
                Row(
                    modifier = Modifier.padding(0.dp, 12.dp)
                ) {
                    Text("VOLUME")

                }

                VolumeControl(modifier, synthesizerViewModel)

            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 32.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            PlayControl(modifier, synthesizerViewModel)


        }

    }

}

@Composable
private fun WavetableSelectionButtons(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp, 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (wavetable in Wavetable.values()) {
            WavetableButton(
                modifier = modifier,
                // update the ViewModel when the given wavetable is clicked
                onClick = {
                    synthesizerViewModel.setWavetable(wavetable)
                },
                // set the label to the resource string that corresponds to the wavetable
                label = stringResource(wavetable.toResourceString()),
            )
        }
    }
}


@Composable
private fun WavetableButton(
    modifier: Modifier,
    onClick: () -> Unit,
    label: String,
) {
    OutlinedButton(modifier = modifier, onClick = onClick) {
        Text(label)
    }
}


@Composable
private fun PitchControl(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {
    // if the frequency changes, recompose this composable
    val frequency = synthesizerViewModel.frequency.observeAsState()
    // the slider position state is hoisted by this composable;
    // no need to embed it into
    // the ViewModel, which, ideally, shouldn't be aware of the UI.
    // When the slider position changes, this composable will be
    // recomposed as we explained in
    // the UI tutorial.
    val sliderPosition = rememberSaveable {
        mutableStateOf(
            // we use the ViewModel's convenience function
            // to get the initial slider position
            synthesizerViewModel.sliderPositionFromFrequencyInHz(frequency.value!!)
        )
    }

    PitchControlContent(
        modifier = modifier,
        value = sliderPosition.value,
        onValueChange = {
            sliderPosition.value = it
            synthesizerViewModel.setFrequencySliderPosition(it)
        },
        // on slider position change, update the slider position and the ViewModel
        valueRange = 0F..1F,
        // this range is now [0, 1] because the ViewModel is
        // responsible for calculating the frequency
        // out of the slider position
        frequencyValueLabel = stringResource(
            R.string.frequency_value,
            frequency.value!!
        )
    )
}


@Composable
private fun PitchControlContent(
    modifier: Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    frequencyValueLabel: String
) {

    Slider(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colors.primary,
            activeTrackColor = MaterialTheme.colors.secondary
        )
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(modifier = modifier, text = frequencyValueLabel)
    }
}

@Composable
private fun PlayControl(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {
    // The label of the play button is now an observable state,
    // an instance of State<Int?>.
    // State<Int?> is used because the label is the id value of the resource string.
    // Thanks to the fact that the composable observes the label,
    // the composable will be recomposed (redrawn) when the observed state changes.
    val playButtonLabel = synthesizerViewModel.playButtonLabel.observeAsState()

    PlayControlContent(
        modifier = modifier,
        // onClick handler now simply notifies the ViewModel that it has been clicked
        onClick = {
            synthesizerViewModel.playClicked()
        },
        // playButtonLabel will never be null;
        // if it is, then we have a serious implementation issue
        buttonLabel = stringResource(playButtonLabel.value!!)
    )
}

@Composable
private fun PlayControlContent(modifier: Modifier, onClick: () -> Unit, buttonLabel: String) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(buttonLabel)
    }
}


@Composable
private fun VolumeControl(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {
    // volume value is now an observable state; that means
    // that the composable will be
    // recomposed (redrawn) when the observed state changes.
    val volume = synthesizerViewModel.volume.observeAsState()

    VolumeControlContent(
        modifier = modifier,
        // volume value should never be null; if it is,
        // there's a serious implementation issue
        volume = volume.value!!,
        // use the value range from the ViewModel
        volumeRange = synthesizerViewModel.volumeRange,
        // on volume slider change, just update the ViewModel
        onValueChange = { synthesizerViewModel.setVolume(it) }
    )
}


@Composable
private fun VolumeControlContent(
    modifier: Modifier,
    volume: Float,
    volumeRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {


    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {


        Slider(
            value = volume,
            onValueChange = onValueChange,
            modifier = modifier,

            valueRange = volumeRange,

            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.primary,
                activeTrackColor = MaterialTheme.colors.secondary
            )
        )


    }

}


