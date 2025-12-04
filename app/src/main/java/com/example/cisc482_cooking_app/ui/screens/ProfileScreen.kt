package com.example.cisc482_cooking_app.ui.screens

// Remove Image and painterResource imports if they are no longer used elsewhere
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons // Import Icons
import androidx.compose.material.icons.filled.AccountCircle // Import the AccountCircle icon
import androidx.compose.material3.*
// import androidx.compose.material3.TextFieldDefaults // This can be removed or kept, but the call site is what matters
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cisc482_cooking_app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    var email by remember { mutableStateOf("jtfulky@udel.edu") }
    var password by remember { mutableStateOf("**********") }
    var firstName by remember { mutableStateOf("John") }
    var lastName by remember { mutableStateOf("Fullkerson") }
    var showDialog by remember { mutableStateOf(false) }
    val simpleBlue = Color(0xFF3B82F6)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(Color(0xFFFADADD))
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(150.dp),
                tint = EspressoBrown
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        val textFieldColors = TextFieldDefaults.colors(
            focusedIndicatorColor = LightGray,
            unfocusedIndicatorColor = LightGray,
            focusedLabelColor = AccentOrange,
            unfocusedLabelColor = AccentOrange,
            cursorColor = AccentOrange,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )

        // --- END OF CHANGE ---

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = AccentOrange, contentColor = Color.White),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Save", modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp))
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Profile Updated", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) },
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = simpleBlue, contentColor = Color.White)
                ) {
                    Text("Ok")
                }
            },
            containerColor = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    CISC482CookingAppTheme {
        ProfileScreen()
    }
}
