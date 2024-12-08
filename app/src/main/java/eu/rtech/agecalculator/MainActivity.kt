package eu.rtech.agecalculator

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgeCalculatorApp()
        }
    }
}

@Composable
fun AgeCalculatorApp() {
    var selectedDate by remember { mutableStateOf("") }
    var ageResult by remember { mutableStateOf("") }
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9))
            .padding(16.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Age Calculator",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF388E3C)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(80.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF388E3C))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Select Birth Date",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {
                            showDatePicker(context) { date ->
                                selectedDate = date
                                ageResult = calculateAge(date)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    selectedDate = ""
                    ageResult = ""
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA5D6A7))
            ) {
                Text("Reset", color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Selected Date: $selectedDate",
                fontSize = 18.sp,
                color = Color.DarkGray,
                modifier = Modifier
                    .animateContentSize()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (ageResult.isNotEmpty()) {
                Text(
                    text = "Your Age: $ageResult",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF388E3C),
                    modifier = Modifier.animateContentSize()
                )
            }
        }
    }
}




fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            onDateSelected("$selectedDay/${selectedMonth + 1}/$selectedYear")
        }, year, month, day
    ).show()
}

fun calculateAge(birthDate: String): String {
    val parts = birthDate.split("/")
    val birthDay = parts[0].toInt()
    val birthMonth = parts[1].toInt() - 1 // Calendar months are 0-based
    val birthYear = parts[2].toInt()

    val today = Calendar.getInstance()
    var ageYears = today.get(Calendar.YEAR) - birthYear
    var ageMonths = today.get(Calendar.MONTH) - birthMonth
    var ageDays = today.get(Calendar.DAY_OF_MONTH) - birthDay

    if (ageDays < 0) {
        ageMonths--
        ageDays += today.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
    if (ageMonths < 0) {
        ageYears--
        ageMonths += 12
    }

    return "$ageYears years, $ageMonths months, $ageDays days"
}
