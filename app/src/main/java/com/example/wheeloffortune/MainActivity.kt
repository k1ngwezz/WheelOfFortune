package com.example.wheeloffortune

import android.app.PendingIntent.getActivity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import com.example.wheeloffortune.ui.theme.WheelOfFortuneTheme
import java.util.*
import kotlin.collections.ArrayList
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.painterResource


class MainActivity : ComponentActivity() {
    var categories = linkedMapOf(
        "Sports" to arrayOf("Bowling", "Cricket", "Football", "Rugby", "Baseball"),
        "Computer Languages" to arrayOf(
            "Java",
            "C++",
            "C#",
            "Go",
            "Ruby",
            "Javascript",
            "Python",
            "Cobalt",
            "Kotlin"
        ),
        "Planets" to arrayOf(
            "Mercury",
            "Venus",
            "Earth",
            "Mars",
            "Jupiter",
            "Saturn",
            "Uranus",
            "Neptune"
        ),
    )
    var rewards = arrayOf("1000", "2000", "3000", "4000", "5000", "bankrupt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun App() {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        )
        {
            val keys = ArrayList(categories.keys)
            var currentSpinReward by rememberSaveable { mutableStateOf("") }
            var currentEnterWord by rememberSaveable { mutableStateOf("") }
            var result by rememberSaveable { mutableStateOf("") }
            var score by rememberSaveable { mutableStateOf(0) }
            var lives by rememberSaveable { mutableStateOf(5) }
            var shouldSpin by rememberSaveable { mutableStateOf(true) }
            var currentCategory by rememberSaveable {
                mutableStateOf(
                    keys.get(
                        Random().nextInt(
                            keys.size
                        )
                    )
                )
            }
            var category by rememberSaveable { mutableStateOf(categories.get(currentCategory)) }
            var word by rememberSaveable {
                mutableStateOf(
                    category!![Random().nextInt(
                        category!!.size
                    )]
                )
            }
            var wordArray by rememberSaveable { mutableStateOf(word.toCharArray()) }
            var seenArray by rememberSaveable { mutableStateOf(BooleanArray(wordArray.size)) }
            Row() {

                Image(
                    painter = painterResource(id = R.drawable.hrt),
                    contentDescription = "",
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
                Text(text = " " + lives, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)

                Spacer(modifier = Modifier.width(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.dgc),
                    contentDescription = "",
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
                Text(
                    text = " $" + score, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = " " + result,
                    fontWeight = FontWeight.ExtraBold, fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.primary,
                )

            }
            Text(
                text = "Category: " + currentCategory,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(), fontSize = 25.sp, fontWeight = FontWeight.Bold
            )
            var rows = (seenArray.size / 5) + 2;
            val height: Int = rows * 40
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier.height(height.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                itemsIndexed(
                    items = wordArray.toTypedArray(),
                ) { index, item ->
                    Text(
                        text = item.toString().uppercase(),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(10.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .background(
                                if (seenArray[index]) Color(0xFF00B603) else Color.White,
                                shape = RoundedCornerShape(5.dp)
                            ),
                        color = if (seenArray[index]) Color.White else Color.Transparent,
                    )
                }
            }
            Button(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .width(100.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    if (lives == 0) {
                        Toast.makeText(
                            this@MainActivity,
                            "Game Over!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    currentSpinReward = rewards.get(Random().nextInt(rewards.size))
                    if (currentSpinReward.equals("Bankrupt")) {
                        score = 0
                        lives--
                        shouldSpin = true;
                    } else {
                        shouldSpin = false;
                    }
                    if (lives == 0) {
                        result = "You Lose :("
                    }
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF304BE2))
            ) {
                Text(
                    text = "Spin",
                    color = Color.White, fontWeight = FontWeight.Bold
                )
            }

            Row {
                Text(
                    text = "You landed on:", fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = " " + currentSpinReward,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )

            }


            val keyboardController = LocalSoftwareKeyboardController.current
            OutlinedTextField(
                value = currentEnterWord,
                onValueChange = {
                    currentEnterWord = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                singleLine = true,
                modifier = Modifier.width(100.dp).defaultMinSize(minHeight = 5.dp),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )
            Button(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .width(100.dp)
                    .height(40.dp),
                onClick = {
                    if (lives == 0) {
                        Toast.makeText(
                            this@MainActivity,
                            "Game Over!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    if (shouldSpin) {
                        Toast.makeText(
                            this@MainActivity,
                            "Spin the wheel!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    if (currentEnterWord.isEmpty()) {
                        Toast.makeText(
                            this@MainActivity,
                            "Enter a letter!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    var isSpellFound = false
                    for (item in wordArray.indices) {
                        if (wordArray[item]
                                .toString().uppercase()
                                .equals(currentEnterWord.uppercase()) && !seenArray[item]
                        ) {
                            seenArray[item] = true
                            seenArray = seenArray.copyOf()
                            score += currentSpinReward.toInt()
                            isSpellFound = true
                        }
                    }
                    if (!isSpellFound) {
                        lives--
                    }
                    if (lives == 0) {
                        result = "You lose!"
                    }
                    if (!seenArray.contains(false)) {
                        result = "You won!"
                    }
                    shouldSpin = true
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF304BE2))
            ) {
                Text(
                    text = "Check",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Button(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                onClick = {
                    shouldSpin = true
                    lives = 5
                    currentSpinReward = ""
                    currentEnterWord = ""
                    result = ""
                    score = 0
                    currentCategory = keys.get(
                        Random().nextInt(
                            keys.size
                        )
                    )
                    category =
                        categories.get(
                            currentCategory
                        )
                    word = category!![Random().nextInt(
                        category!!.size
                    )]
                    wordArray = word.toCharArray()
                    seenArray = BooleanArray(
                        wordArray.size
                    )
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF304BE2))
            ) {
                Text(
                    text = "Restart",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

        }


    }
}
