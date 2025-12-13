package com.example.cisc482_cooking_app.ui.components

import android.R.attr.alpha
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import com.example.cisc482_cooking_app.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cisc482_cooking_app.ui.theme.AccentOrange
import com.example.cisc482_cooking_app.ui.theme.Cream
import com.example.cisc482_cooking_app.ui.theme.EspressoBrown
import com.example.cisc482_cooking_app.ui.theme.LightGray

@Composable
fun RecipeCard(recipe: RecipeData, onStartClick: (String) -> Unit) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    val rotation: Float by animateFloatAsState(if (isExpanded) 270f else 180f, label = "")
    val imageResId = remember(recipe.img) {
        context.resources.getIdentifier(
            recipe.img,
            "drawable",
            context.packageName
        )
    }
    CompositionLocalProvider(LocalContentColor provides LightGray) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(EspressoBrown)
                .clickable { isExpanded = !isExpanded }
        ) {
            Image(
                painter = painterResource(id = imageResId),
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
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(recipe.name, fontSize = 30.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(30.dp),
                            shape = CircleShape,
                            border = BorderStroke(2.dp, Color.Green),
                            color = Color.Green.copy(alpha = 0.3f)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(recipe.ingredients.size.toString())
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
                    AnimatedVisibility(visible = isExpanded) {
                        HorizontalDivider(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                            color = LightGray,
                            thickness = 2.dp)
                        Column {
                            Row(modifier = Modifier.height(IntrinsicSize.Min)){
                                Column(modifier = Modifier.padding(8.dp)) {
                                    recipe.ingredients.forEach { ingredient ->
                                        Text(text = "- $ingredient")
                                    }
                                }
                                VerticalDivider(modifier = Modifier.fillMaxHeight(),
                                    color = LightGray,
                                    thickness = 2.dp,)
                                Column(modifier = Modifier.padding(8.dp)) {
                                    recipe.tools.forEach { tool ->
                                        Text(text = "- $tool")
                                    }
                                }
                            }
                            Text(
                                text = recipe.description,
                                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                            )
                            Row(modifier = Modifier.height(IntrinsicSize.Min)){
                                Button(onClick = { onStartClick(recipe.name) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AccentOrange,
                                        contentColor = Color.White)) {
                                    Row{
                                        Text(text = "Start")
                                        Icon(
                                            imageVector = Icons.Default.ArrowCircleRight,
                                            contentDescription = "Start Button",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                }


                            }
                        }
                    }
                }
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Go to recipe",
                    modifier = Modifier
                        .rotate(rotation)
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewRecipeCard(){
    RecipeCard(pBJData, {})
}
