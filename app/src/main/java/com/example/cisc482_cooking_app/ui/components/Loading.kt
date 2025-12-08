package com.example.cisc482_cooking_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.cisc482_cooking_app.ui.theme.AccentOrange
import com.example.cisc482_cooking_app.ui.theme.Cream
import com.example.cisc482_cooking_app.ui.theme.EspressoBrown
import com.example.cisc482_cooking_app.ui.theme.LightGray

/** Pop-up loading indicator that matches the app's cream + orange palette. */
@Composable
fun LoadingPopup(message: String = "Cooking up your recipe...") {
	Dialog(onDismissRequest = { }) {
		Surface(
			color = Cream,
			shape = MaterialTheme.shapes.large,
			tonalElevation = 8.dp
		) {
			Column(
				modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center
			) {
				CircularProgressIndicator(
					color = AccentOrange,
					trackColor = LightGray
				)
				Spacer(modifier = Modifier.height(16.dp))
				Text(
					text = message,
					style = MaterialTheme.typography.bodyMedium,
					color = EspressoBrown,
					textAlign = TextAlign.Center
				)
			}
		}
	}
}