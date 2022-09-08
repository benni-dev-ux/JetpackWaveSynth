package com.example.jetpackwavesynth


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.jetpackwavesynth.ui.theme.JetpackWaveSynthTheme
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController: SystemUiController = rememberSystemUiController()

            systemUiController.isSystemBarsVisible = false // Status & Navigation bars

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
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,

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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 32.dp),
    ) {
        Text("WAVETABLE")
        WavetableSelectionButtons(modifier)
    }

}

@Composable
private fun ControlsPanel(
    modifier: Modifier
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
               Row (
                   modifier = Modifier.padding(0.dp,12.dp)
                       ){
                   Text("FREQUENCY")

               }

                PitchControl(modifier)


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
                Row (
                    modifier = Modifier.padding(0.dp,12.dp)
                ){
                    Text("VOLUME")

                }

                VolumeControl(modifier)

            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 32.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            PlayControl(modifier)


        }

    }

}


@Composable
private fun WavetableSelectionButtons(
    modifier: Modifier
) {
    Row(


        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp, 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        for (wavetable in arrayOf("SINE", "TRIANGLE", "SQUARE", "SAW")) {
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
    OutlinedButton(modifier = modifier, onClick = onClick) {
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
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    frequencyValueLabel: String
) {
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
    Button(modifier = modifier.width(300.dp),

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


    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {


        Slider(
            value = volume,
            onValueChange = onValueChange,
            modifier = modifier,

            valueRange = volumeRange
        )


    }

}


