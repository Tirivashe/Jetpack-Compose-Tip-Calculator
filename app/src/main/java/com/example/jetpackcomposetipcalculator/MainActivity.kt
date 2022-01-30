package com.example.jetpackcomposetipcalculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposetipcalculator.components.InputField
import com.example.jetpackcomposetipcalculator.ui.theme.JetpackComposeTipCalculatorTheme
import com.example.jetpackcomposetipcalculator.utils.calculateTip
import com.example.jetpackcomposetipcalculator.utils.calculateTotalBillPerPerson
import com.example.jetpackcomposetipcalculator.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetpackComposeTipCalculatorTheme {
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}

//@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 134.0){
    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(all = 15.dp)
        .height(150.dp)
        .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color(0xFFE9D7F)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.h5,
            )
            Text(
                text = "$${"%.2f".format(totalPerPerson)}",
                style = MaterialTheme.typography.h4,
                fontWeight =  FontWeight.ExtraBold
            )
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent(){
    val totalBillState = remember{
        mutableStateOf("")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val numOfPeople = remember {
        mutableStateOf(1)
    }

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }

    val totalBillPerPerson = remember {
        mutableStateOf(0.00)
    }
    Column() {
        TopHeader(totalBillPerPerson.value)
        BillForm(
            totalBillPerPerson = totalBillPerPerson,
            totalBillState = totalBillState,
            validState = validState,
            numOfPeople = numOfPeople,
            sliderPositionState = sliderPositionState,
            tipAmountState = tipAmountState
        ){ billValue ->
            Log.d("Total Bill", "MainContent: $billValue")
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    totalBillPerPerson: MutableState<Double>,
    totalBillState: MutableState<String>,
    validState: Boolean,
    numOfPeople: MutableState<Int>,
    sliderPositionState: MutableState<Float>,
    tipAmountState: MutableState<Double>,
    onValChange: (String) -> Unit){

    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),

        ) {
        Column(
            modifier = modifier
                .padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                isEnabled = true,
                isSingleLine = true,
                onActions = KeyboardActions{
                    if(!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())

                    keyboardController?.hide()
                }
            )

            if(validState){
                Row(
                    modifier = modifier
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.Start,

                ) {
                    Text(
                        text = "Split",
                        modifier = modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Spacer(modifier = modifier.width(120.dp))
                    Row(
                        modifier = modifier
                            .padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIconButton(
                            imageVector = Icons.Default.Remove,
                            onClick = {
                                if(numOfPeople.value != 1) {
                                    numOfPeople.value --
                                    totalBillPerPerson.value =
                                        calculateTotalBillPerPerson(
                                            totalBillAmount = totalBillState.value.toDouble(),
                                            tipPercentage = sliderPositionState.value.toDouble(),
                                            numOfPeople = numOfPeople.value
                                        )
                                }

                            }
                        )
                        Text(
                            text = numOfPeople.value.toString(),
                            modifier = modifier
                                .align(Alignment.CenterVertically)
                                .padding(horizontal = 9.dp)
                        )
                        RoundIconButton(
                            imageVector = Icons.Default.Add,
                            onClick = {
                                numOfPeople.value ++
                                totalBillPerPerson.value =
                                    calculateTotalBillPerPerson(
                                        totalBillAmount = totalBillState.value.toDouble(),
                                        tipPercentage = sliderPositionState.value.toDouble(),
                                        numOfPeople = numOfPeople.value
                                    )
                            }
                        )
                    }
                }

                Row(
                    modifier = modifier
                        .padding(horizontal = 3.dp, vertical = 12.dp)
                ) {
                    Text(text = "Tip", modifier = modifier.align(Alignment.CenterVertically))
                    Spacer(modifier = modifier.width(200.dp))
                    Text(text = "$${"%.2f".format(tipAmountState.value)}", modifier = modifier.align(Alignment.CenterVertically))
                }
            
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "${(sliderPositionState.value * 100).toInt()}%", modifier.align(Alignment.CenterHorizontally))
                    Spacer(modifier = modifier.height(14.dp))
                    Slider(
                        modifier = modifier.padding(horizontal = 16.dp),
                        value = sliderPositionState.value,
                        steps = 5,
                        onValueChange = {
                            sliderPositionState.value = it
                            tipAmountState.value =
                                calculateTip(
                                    totalBillAmount = totalBillState.value.toDouble(),
                                    tipPercentage = sliderPositionState.value.toDouble()
                                )
                            totalBillPerPerson.value =
                                calculateTotalBillPerPerson(
                                    totalBillAmount = totalBillState.value.toDouble(),
                                    tipPercentage = sliderPositionState.value.toDouble(),
                                    numOfPeople = numOfPeople.value
                                )
                        }
                    )
                }
            }else{
                Box() {}
            }
        }
    }
}
