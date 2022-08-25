package com.example.jetpackwavesynth


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.jetpackwavesynth.ui.theme.JetpackWaveSynthTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackWaveSynthTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    WavetableSynthesizerApp(Modifier)
                }
            }
        }
    }
}


@Composable
fun WavetableSynthesizerApp(
    modifier: Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        // These two composables will be shortly defined
        WavetableSelectionPanel(modifier)
        ControlsPanel(modifier)
    }
}

@Composable
private fun WavetableSelectionPanel(
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Wavetable selection panel")
            WavetableSelectionButtons(modifier)
        }
    }
}

@Composable
private fun ControlsPanel(
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth(0.7f), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PitchControl(modifier)
            PlayControl(modifier)
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            VolumeControl(modifier)
        }
    }
}


@Composable
private fun WavetableSelectionButtons(
    modifier: Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (wavetable in arrayOf("Sine", "Triangle", "Square", "Saw")) {
            WavetableButton(
                modifier = modifier,
                onClick = {},
                label = wavetable
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
    Button(modifier = modifier, onClick = onClick) {
        Text(label)
    }
}


@Composable
private fun PitchControl(
    modifier: Modifier
) {
    val sliderPosition = rememberSaveable { mutableStateOf(300F) }

    PitchControlContent(
        modifier = modifier,
        pitchControlLabel = stringResource(R.string.frequency),
        value = sliderPosition.value,
        onValueChange = {
            sliderPosition.value = it
        },
        valueRange = 20F..3000F,
        frequencyValueLabel = stringResource(
            R.string.frequency_value,
            sliderPosition.value
        )
    )
}

@Composable
private fun PitchControlContent(
    modifier: Modifier,
    pitchControlLabel: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    frequencyValueLabel: String
) {
    Text(pitchControlLabel, modifier = modifier)
    Slider(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(modifier = modifier, text = frequencyValueLabel)
    }
}

@Composable
private fun PlayControl(modifier: Modifier) {
    Button(modifier = modifier,
        onClick = {}) {
        Text(stringResource(R.string.play))
    }
}


@Composable
private fun VolumeControl(modifier: Modifier) {
    val volume = rememberSaveable { mutableStateOf(0F) }

    VolumeControlContent(
        modifier = modifier,
        volume = volume.value,
        volumeRange = -60F..0F,
        onValueChange = { volume.value = it })
}

@Composable
private fun VolumeControlContent(
    modifier: Modifier,
    volume: Float,
    volumeRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    // The volume slider should take around 1/4 of the screen height
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val sliderHeight = screenHeight / 4


    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = Icons.Filled.VolumeUp, contentDescription = null)


        Slider(
            value = volume,
            onValueChange = onValueChange,
            modifier = modifier
                .width(sliderHeight.dp),
            valueRange = volumeRange
        )

        Icon(imageVector = Icons.Filled.VolumeMute, contentDescription = null)
    }

}


