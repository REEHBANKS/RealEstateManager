package com.openclassrooms.realestatemanager.view.activity


import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openclassrooms.realestatemanager.viewmodel.PropertyDetailViewModel
import com.openclassrooms.realestatemanager.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class SearchActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                FilterScreen()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FilterScreen() {
    val searchViewModel: SearchViewModel = hiltViewModel()
    val selectedOptionType = remember { mutableStateOf<String?>(null) }
    val minValue = remember { mutableStateOf<Int?>(null) }
    val maxValue = remember { mutableStateOf<Int?>(null) }
    val selectedProximityItems = remember { mutableStateOf(setOf<String>()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val minPrice = remember { mutableStateOf<Int?>(null) }
    val maxPrice = remember { mutableStateOf<Int?>(null) }
    var filterByPhotoCount by remember { mutableStateOf(false) }
    val selectedNeighborhood = remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filter") },
                navigationIcon = {
                    // Handle navigation icon press
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // If you have any actions, they go here.
                }
            )

        },
        bottomBar = {
            SearchActionBar(
                onValidate = {

                    val minSurface = minValue.value ?: 0
                    val maxSurface = maxValue.value ?: Int.MAX_VALUE
                    val minP = minPrice.value ?: 0
                    val maxP = maxPrice.value ?: Int.MAX_VALUE

                    if (minSurface > maxSurface) {
                        Toast.makeText(context, "La surface minimale ne peut pas être supérieure à la surface maximale.", Toast.LENGTH_LONG).show()
                    } else if (minP > maxP) {
                        Toast.makeText(context, "Le prix minimal ne peut pas être supérieur au prix maximal.", Toast.LENGTH_LONG).show()
                    } else {
                        searchViewModel.performSearch (
                            selectedOptionType = selectedOptionType.value,
                            minSurface = minSurface,
                            maxSurface = maxSurface,
                            minPrice = minP,
                            maxPrice = maxP,
                            proximity = selectedProximityItems.value,
                            date = selectedDate,
                            filterByPhotoCount = filterByPhotoCount,
                            neighborhood = selectedNeighborhood.value
                        )

                    }

                    if (context is Activity) {
                        context.finish()
                    }

                },
                onReset = {
                    selectedOptionType.value = null
                    minValue.value = null
                    maxValue.value = null
                    minPrice.value = null
                    maxPrice.value = null
                    selectedProximityItems.value = setOf()
                    selectedDate = null
                    filterByPhotoCount = false
                    selectedNeighborhood.value = null


                    searchViewModel.performSearch(
                        null,
                        0,
                        Int.MAX_VALUE,
                        0,
                        Int.MAX_VALUE,
                        setOf(),
                        null,
                        false,
                        null
                    )

                }
            )
        }


    )





    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            // "Type" section title
            Text(
                text = "Type",
                style = MaterialTheme.typography.h6.merge(
                    TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

            )


            // Chip row for property types
            // Here you should place your FilterChip composable
            FilterChipRow(selectedOption = selectedOptionType)


            Divider(modifier = Modifier.padding(top = 5.dp)) // ... rest of your content

            Text(
                text = "Surface (m2)",
                style = MaterialTheme.typography.h6.merge(
                    TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

            )
            MinMaxInputFields(minValueState = minValue, maxValueState = maxValue  )

            Divider(modifier = Modifier.padding(top = 0.dp))

            Text(
                text = "Proximity",
                style = MaterialTheme.typography.h6.merge(
                    TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

            )
            MultipleFilterChipRow(selectedItems = selectedProximityItems)

            Divider(modifier = Modifier.padding(top = 15.dp))

            Text(
                text = "On Market Since",
                style = MaterialTheme.typography.h6.merge(
                    TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

            )

            Box(
                contentAlignment = Alignment.Center, // Centre le contenu dans la Box
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                Button(
                    onClick = {
                        val datePickerDialog = DatePickerDialog(
                            context,
                            { _, year, monthOfYear, dayOfMonth ->

                                selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                            },
                            LocalDate.now().year, // Année actuelle
                            LocalDate.now().monthValue - 1, // Mois actuel (mois - 1 car DatePickerDialog utilise 0-11 pour les mois)
                            LocalDate.now().dayOfMonth // Jour actuel
                        )
                        datePickerDialog.show()

                    }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                    modifier = Modifier
                        .align(Alignment.Center)

                ) {
                    Text(
                        "Select Date",
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }
            }

            // Affichage de la date sélectionnée
            selectedDate?.let {
                Text("Selected date: ${it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}")
            }

            Divider(modifier = Modifier.padding(top = 15.dp))

            Text(
                text = "Price (€)",
                style = MaterialTheme.typography.h6.merge(
                    TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

            )
            PriceRangeInputFields(minPriceState = minPrice, maxPriceState = maxPrice)

            Divider(modifier = Modifier
                .padding(top = 0.dp)
                .padding(bottom = 0.dp)

            )

            Text(
                text = "Pictures",
                style = MaterialTheme.typography.h6.merge(
                    TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(1f),

            ) {
                Text(
                    text = "At least 3 photos",
                    style = MaterialTheme.typography.h6.merge(
                        TextStyle(
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    ),
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = filterByPhotoCount,
                    onCheckedChange = { filterByPhotoCount = it }
                )


            }
            Divider(modifier = Modifier.padding(top = 0.dp))

            Text(
                text = "Neighborhood",
                style = MaterialTheme.typography.h6.merge(
                    TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

            )
            NeighborhoodFilter { neighborhood ->
                selectedNeighborhood.value = neighborhood
            }



        }


    }
}

@Composable
fun FilterChipRow(selectedOption: MutableState<String?>) {

    // This is just a placeholder for your chip layout logic
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        FilterChip(text = "flat", isSelected = selectedOption.value == "flat") {
            selectedOption.value = if (selectedOption.value == "flat") null else "flat"
        }
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(text = "loft", isSelected = selectedOption.value == "loft") {
            selectedOption.value = if (selectedOption.value == "loft") null else "loft"
        }
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(text = "duplex", isSelected = selectedOption.value == "duplex") {
            selectedOption.value = if (selectedOption.value == "duplex") null else "duplex"
        }
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(text = "house", isSelected = selectedOption.value == "house") {
            selectedOption.value = if (selectedOption.value == "house") null else "house"
        }
        // Add more chips as needed
    }
}

@Composable
fun FilterChip(text: String, isSelected: Boolean, onSelected: () -> Unit) {
    Surface(
        modifier = Modifier
            .width(70.dp) // Largeur fixe pour le chip
            .height(35.dp) // Hauteur fixe pour le chip
            .clip(RoundedCornerShape(20)) // Coins arrondis pour un look de chip
            .clickable(onClick = onSelected), // Rend le chip cliquable
        color = if (isSelected) Color.Blue else Color.Transparent,
        border = BorderStroke(1.dp, Color.Blue)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                color = if (isSelected) Color.White else Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun MinMaxInputFields(minValueState: MutableState<Int?>,maxValueState: MutableState<Int?> ) {
    // État pour stocker et contrôler les valeurs min et max


    Box(
        modifier = Modifier
            .height(90.dp)
            .padding(bottom = 1.dp)
            .padding(top = 2.dp)
            .offset(y = (-5).dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .clip(RoundedCornerShape(20)),
            horizontalArrangement = Arrangement.spacedBy(25.dp)
        ) {

            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(55.dp)
                    .border(1.dp, Color.Blue)
                    .offset(y = (-10).dp)
                    .padding(1.dp)
            ) {
                TextField(
                    value = minValueState.value?.toString() ?: "",
                    onValueChange = { newValue ->
                        val newMin = newValue.toIntOrNull()
                        minValueState.value = newMin
                    },
                    textStyle = TextStyle(
                        color = Color.Gray,
                        fontSize = 13.sp
                    ),
                    placeholder = { Text(text = "1500", fontSize = 13.sp) },
                    singleLine = true,
                    modifier = Modifier.width(120.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    visualTransformation = VisualTransformation.None,
                    maxLines = 1
                )

            }

            Box(
                modifier = Modifier
                    .width(70.dp) // Largeur fixe pour le chip
                    .height(55.dp) // Hauteur fixe pour le chip
                    .border(1.dp, Color.Blue)
                    .offset(y = (-10).dp)
                    .padding(1.dp)
            ) {
                TextField(
                    value = maxValueState.value?.toString() ?: "",
                    onValueChange = { newValue ->
                        val newMax = newValue.toIntOrNull()
                        maxValueState.value = newMax
                    },
                    textStyle = TextStyle(
                        color = Color.Gray,
                        fontSize = 13.sp
                    ),
                    placeholder = { Text(text = "1500", fontSize = 13.sp) },
                    singleLine = true,
                    modifier = Modifier.width(120.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    // Supprimez le padding par défaut du TextField pour plus d'espace
                    visualTransformation = VisualTransformation.None,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun MultipleFilterChipRow(selectedItems: MutableState<Set<String>>) {
    val chipLabels = listOf("schools", "shops", "parks", "station")

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {

        chipLabels.forEach { label ->
            FilterChip(
                text = label,
                isSelected = selectedItems.value.contains(label),
                onSelected = {

                    val currentSelection = selectedItems.value.toMutableSet()
                    if (currentSelection.contains(label)) {
                        currentSelection.remove(label)
                    } else {
                        currentSelection.add(label)
                    }
                    selectedItems.value = currentSelection
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun PriceRangeInputFields(minPriceState: MutableState<Int?>, maxPriceState: MutableState<Int?>) {
    Box(
        modifier = Modifier
            .height(90.dp)
            .padding(bottom = 1.dp)
            .padding(top = 2.dp)
            .offset(y = (-10).dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .clip(RoundedCornerShape(0)),
            horizontalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            // Champ pour le prix minimal
            PriceInputField(value = minPriceState.value?.toString() ?: "", onValueChange = { newValue ->
                minPriceState.value = newValue.toIntOrNull()
            }, placeholder = "Min Price")

            // Champ pour le prix maximal
            PriceInputField(value = maxPriceState.value?.toString() ?: "", onValueChange = { newValue ->
                maxPriceState.value = newValue.toIntOrNull()
            }, placeholder = "Max Price")
        }
    }
}


@Composable
fun PriceInputField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Box(
        modifier = Modifier
            .width(70.dp)
            .height(55.dp)
            .border(1.dp, Color.Blue)
            .padding(1.dp)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = Color.Gray,
                fontSize = 13.sp
            ),
            placeholder = { Text(text = "1000", fontSize = 13.sp) },
            singleLine = true,
            modifier = Modifier.width(120.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            visualTransformation = VisualTransformation.None,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun NeighborhoodFilter(onNeighborhoodFilterChanged: (String) -> Unit) {
    var neighborhood by remember { mutableStateOf("") }

    TextField(
        value = neighborhood,
        onValueChange = { newValue ->
            neighborhood = newValue
            onNeighborhoodFilterChanged(newValue.trim())
        },
        placeholder = { Text(text = "Ile de France", fontSize = 13.sp) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp)
            .offset(y = (-5).dp)
            .padding(5.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent, // Arrière-plan transparent

        ),
        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
    )
}

@Composable
fun SearchActionBar(
    onValidate: () -> Unit,
    onReset: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Search reset button
        ActionButton(
            text = "Reset",
            onClick = onReset,
            backgroundColor = Color.LightGray, // Utilisez une couleur différente pour distinguer le bouton
            contentColor = Color.Black
        )
        // Search reset button
        Spacer(modifier = Modifier.width(8.dp))

        ActionButton(
            text = "Validate",
            onClick = onValidate,
            backgroundColor = Color.Blue, // Utilisez une couleur différente pour distinguer le bouton
            contentColor = Color.White
        )
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color,
    contentColor: Color
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        modifier = Modifier
            .height(48.dp)

    ) {
        Text(
            text = text,
            color = contentColor
        )
    }
}




@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    FilterScreen()

}
