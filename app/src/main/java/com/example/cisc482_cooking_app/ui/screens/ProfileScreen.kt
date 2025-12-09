package com.example.cisc482_cooking_app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cisc482_cooking_app.model.Allergy
import com.example.cisc482_cooking_app.model.User
import com.example.cisc482_cooking_app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User,
    onUserChange: (User) -> Unit
) {
    // ---------- Local UI state (can be temporarily invalid) ----------
    var nameInput by remember { mutableStateOf(user.name) }
    var emailInput by remember { mutableStateOf(user.email) }
    var password by remember { mutableStateOf("**********") }

    var selectedAllergies by remember { mutableStateOf(user.allergies) }
    var customAllergyInput by remember { mutableStateOf(user.customAllergy ?: "") }

    var showDialog by remember { mutableStateOf(false) }

    // If parent passes a new user, sync the local UI state with it
    LaunchedEffect(user) {
        nameInput = user.name
        emailInput = user.email
        selectedAllergies = user.allergies
        customAllergyInput = user.customAllergy ?: ""
    }

    // ---------- Validation based on UI state ----------
    val isNameInvalid = nameInput.isBlank()
    val isEmailInvalid = !(emailInput.contains("@") && emailInput.contains("."))
    val isOtherSelected = selectedAllergies.contains(Allergy.OTHER)
    // The custom allergy field is invalid only if "Other" is selected AND the text is blank.
    val isCustomAllergyInvalid = isOtherSelected && customAllergyInput.isBlank()

    val isSaveButtonEnabled = !isNameInvalid && !isEmailInvalid && !isCustomAllergyInvalid

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // ---------- Avatar ----------
        item {
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
        }

        // ---------- Form fields ----------
        item {
            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LightGray,
                unfocusedBorderColor = LightGray,
                focusedLabelColor = AccentOrange,
                unfocusedLabelColor = AccentOrange,
                cursorColor = AccentOrange,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )

            // EMAIL
            OutlinedTextField(
                value = emailInput,
                onValueChange = { emailInput = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                colors = textFieldColors,
                isError = isEmailInvalid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            if (isEmailInvalid) {
                Text(
                    text = "Please enter a valid email",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // PASSWORD (just UI – not changing hashedPassword here)
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

            // NAME
            OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                colors = textFieldColors,
                isError = isNameInvalid
            )
            if (isNameInvalid) {
                Text(
                    text = "Name cannot be blank",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // ---------- Allergies ----------
        item {
            AllergiesSection(
                selectedAllergies = selectedAllergies,
                customAllergyText = customAllergyInput,
                isCustomAllergyInvalid = isCustomAllergyInvalid, // Pass down the invalid flag
                onAllergyChange = { allergy, isSelected ->
                    selectedAllergies = if (isSelected) {
                        (selectedAllergies + allergy).distinct()
                    } else {
                        selectedAllergies - allergy
                    }
                },
                onCustomAllergyTextChange = { newText ->
                    customAllergyInput = newText
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        // ---------- Save button ----------
        item {
            Button(
                onClick = {
                    // Only construct a new User when everything is valid
                    if (isSaveButtonEnabled) {
                        val updatedUser = user.copy(
                            name = nameInput.trim(),
                            email = emailInput.trim(),
                            allergies = selectedAllergies,
                            customAllergy = if (selectedAllergies.contains(Allergy.OTHER)) {
                                customAllergyInput.trim()
                            } else {
                                null
                            }
                        )
                        onUserChange(updatedUser)
                        showDialog = true
                    }
                },
                enabled = isSaveButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentOrange,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Save", modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp))
            }
        }
    }

    // ---------- Success dialog ----------
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "Profile Updated",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6),
                        contentColor = Color.White
                    )
                ) {
                    Text("Ok")
                }
            },
            containerColor = Color.White
        )
    }
}

// ---------- Allergies UI ----------

@Composable
fun AllergiesSection(
    selectedAllergies: List<Allergy>,
    customAllergyText: String,
    isCustomAllergyInvalid: Boolean, // Receive flag to show error state
    onAllergyChange: (Allergy, Boolean) -> Unit,
    onCustomAllergyTextChange: (String) -> Unit
) {
    val standardAllergies = Allergy.entries.filter { it != Allergy.OTHER }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, LightGray),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp) // Spacing between rows
        ) {
            val chunkedAllergies = standardAllergies.chunked(2)
            chunkedAllergies.forEachIndexed { index, rowAllergies ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Display the regular allergy checkbox (e.g., Soy, Eggs, etc.)
                    AllergyCheckbox(
                        allergy = rowAllergies[0],
                        isChecked = selectedAllergies.contains(rowAllergies[0]),
                        onCheckedChange = { isChecked ->
                            onAllergyChange(rowAllergies[0], isChecked)
                        },
                        modifier = Modifier.weight(1f)
                    )


                    if (index == chunkedAllergies.lastIndex && rowAllergies.size == 1) {

                        OutlinedTextField(
                            value = customAllergyText,
                            onValueChange = onCustomAllergyTextChange,
                            label = { Text("Other") },
                            modifier = Modifier.weight(1f),
                            isError = isCustomAllergyInvalid,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentOrange,
                                unfocusedBorderColor = LightGray,
                                cursorColor = AccentOrange
                            )
                        )
                    } else {
                        // Otherwise, display the second allergy checkbox as normal
                        AllergyCheckbox(
                            allergy = rowAllergies[1],
                            isChecked = selectedAllergies.contains(rowAllergies[1]),
                            onCheckedChange = { isChecked ->
                                onAllergyChange(rowAllergies[1], isChecked)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AllergyCheckbox(
    allergy: Allergy,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = AccentOrange,
                uncheckedColor = EspressoBrown
            )
        )
        val allergyText = allergy.name
            .replace('_', ' ')
            .lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        Text(
            text = allergyText,
            color = EspressoBrown,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    CISC482CookingAppTheme {
        ProfileScreen(
            user = User(
                id = "1",
                name = "Preview User",
                email = "preview@email.com",
                hashedPassword = "hash",
                allergies = listOf(Allergy.MILK, Allergy.OTHER),
                customAllergy = "Strawberries"
            ),
            onUserChange = {}
        )
    }
}
