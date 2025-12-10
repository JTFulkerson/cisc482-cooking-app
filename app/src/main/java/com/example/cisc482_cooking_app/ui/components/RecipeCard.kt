package com.example.cisc482_cooking_app.ui.components

import android.R.attr.alpha
import androidx.compose.foundation.BorderStroke
import com.example.cisc482_cooking_app.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cisc482_cooking_app.ui.theme.Cream
import com.example.cisc482_cooking_app.ui.theme.EspressoBrown
import com.example.cisc482_cooking_app.ui.theme.LightGray

@Composable
fun RecipeCard(recipe: RecipeData) {
    CompositionLocalProvider(LocalContentColor provides LightGray) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .background(EspressoBrown)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pbj),
                contentDescription = "Peanut Butter & Jelly",
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize(),
                contentScale = ContentScale.Crop,
                alpha = .4f
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    Text(recipe.name, fontSize = 30.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(30.dp)
                                .clip(CircleShape),
                            color = Color.Green.copy(alpha = 0.3f),        // transparent background
                            border = BorderStroke(2.dp, Color.Green),      // solid green stroke
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(recipe.ingredients.size.toString())    // stays fully opaque
                            }
                        }

                        Text("Ingredients")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Time to cook",
                            modifier = Modifier
                                .padding(8.dp)
                                .size(30.dp)
                        )
                        Text(text = "${recipe.time} min")
                    }
                }
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Go to recipe",
                    modifier = Modifier
                        .rotate(180f)
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewRecipeCard(){
    RecipeCard(pBJData)
}