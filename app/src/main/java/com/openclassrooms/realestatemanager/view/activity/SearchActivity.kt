package com.openclassrooms.realestatemanager.view.activity


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale


class MyComposeActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FilterScreen()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FilterScreen() {
    val selectedProximityItems = remember { mutableStateOf(setOf<String>()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var filterByPhotoCount by remember { mutableStateOf(false) }
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
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // "Type" section title
            Text(
                text = "Type",
                style = MaterialTheme.typography.h6.merge(
                    androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

            )


            // Chip row for property types
            // Here you should place your FilterChip composable
            FilterChipRow()

            Divider(modifier = Modifier.padding(top = 30.dp)) // ... rest of your content

            Text(
                text = "Surface (m2)",
                style = MaterialTheme.typography.h6.merge(
                    androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

            )
            MinMaxInputFields()

            Divider(modifier = Modifier.padding(top = 0.dp))

            Text(
                text = "Proximity",
                style = MaterialTheme.typography.h6.merge(
                    androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

            )
            MultipleFilterChipRow(selectedItems = selectedProximityItems)

            Divider(modifier = Modifier.padding(top = 30.dp))

            Text(
                text = "On Market Since",
                style = MaterialTheme.typography.h6.merge(
                    androidx.compose.ui.text.TextStyle(
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
                                // Mise à jour de la date sélectionnée
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

            Divider(modifier = Modifier.padding(top = 30.dp))

            Text(
                text = "Price (€)",
                style = MaterialTheme.typography.h6.merge(
                    androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

            )
            PriceRangeInputFields()

            Divider(modifier = Modifier.padding(top = 0.dp))

            Text(
                text = "Pictures",
                style = MaterialTheme.typography.h6.merge(
                    androidx.compose.ui.text.TextStyle(
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
                        androidx.compose.ui.text.TextStyle(
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
                    androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

            )
            NeighborhoodFilter { neighborhood ->
                // je recupere la valeur de neighborhodd et le donne à vm
            }


        }


    }
}

@Composable
fun FilterChipRow() {
    // This is just a placeholder for your chip layout logic
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        FilterChip(text = "Flat", isSelected = true, onSelected = { /* TODO */ })
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(text = "Loft", isSelected = false, onSelected = { /* TODO */ })
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(text = "Duplex", isSelected = false, onSelected = { /* TODO */ })
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(text = "House", isSelected = false, onSelected = { /* TODO */ })
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
fun MinMaxInputFields() {
    // État pour stocker et contrôler les valeurs min et max
    var minValue by remember { mutableStateOf("") }
    var maxValue by remember { mutableStateOf("") }

    // Convertir en nombres pour la comparaison, en traitant les entrées vides comme zéro
    val minNumber = minValue.toIntOrNull() ?: 0
    val maxNumber = maxValue.toIntOrNull() ?: Int.MAX_VALUE

    // Fonction de validation pour s'assurer que max n'est pas inférieur à min
    fun validateValues() {
        if (minNumber > maxNumber) {
            // Réinitialiser les champs si la validation échoue
            minValue = ""
            maxValue = ""
        }
    }

    Box(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(), // Remplit la taille maximale du parent
        contentAlignment = Alignment.Center // Centre le contenu à l'intérieur du Box
    ) {

        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .clip(RoundedCornerShape(20)), // Coins arrondis pour un look de chip,
            horizontalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            // Champ pour la valeur minimale
            Box(
                modifier = Modifier
                    .width(70.dp) // Largeur fixe pour le chip
                    .height(55.dp) // Hauteur fixe pour le chip
                    .border(1.dp, Color.Blue)
                    .padding(1.dp)
            ) {
                TextField(
                    value = minValue,
                    onValueChange = { newValue ->
                        if (newValue.all { char -> char.isDigit() }) {
                            minValue = newValue
                            validateValues()
                        }
                    },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = Color.Gray,
                        fontSize = 13.sp
                    ),
                    placeholder = { Text(text = "1500", fontSize = 13.sp) },
                    singleLine = true,
                    modifier = Modifier.width(120.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent, // Pour un arrière-plan transparent
                        unfocusedIndicatorColor = Color.Transparent, // Pas de ligne en dessous quand il n'est pas focus
                        focusedIndicatorColor = Color.Transparent
                    ),
                    // Supprimez le padding par défaut du TextField pour plus d'espace
                    visualTransformation = VisualTransformation.None,
                    maxLines = 1
                )

            }
            // Champ pour la valeur maximale
            Box(
                modifier = Modifier
                    .width(70.dp) // Largeur fixe pour le chip
                    .height(55.dp) // Hauteur fixe pour le chip
                    .border(1.dp, Color.Blue)
                    .padding(1.dp)
            ) {
                TextField(
                    value = maxValue,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) { // Accepter seulement les chiffres
                            maxValue = it
                            validateValues()
                        }
                    },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = Color.Gray,
                        fontSize = 13.sp
                    ),
                    placeholder = { Text(text = "1500", fontSize = 13.sp) },
                    singleLine = true,
                    modifier = Modifier.width(120.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent, // Pour un arrière-plan transparent
                        unfocusedIndicatorColor = Color.Transparent, // Pas de ligne en dessous quand il n'est pas focus
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
            .padding(top = 15.dp) // Ajouter de l'espace entre le texte "Proximity" et la Row
    ) {
        // Créez un FilterChip pour chaque label
        chipLabels.forEach { label ->
            FilterChip(
                text = label,
                isSelected = selectedItems.value.contains(label),
                onSelected = {
                    // Mise à jour de l'état de sélection
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
fun PriceRangeInputFields() {
    var minPrice by remember { mutableStateOf("") }
    var maxPrice by remember { mutableStateOf("") }

    val minPriceNumber = minPrice.toIntOrNull() ?: 0
    val maxPriceNumber = maxPrice.toIntOrNull() ?: Int.MAX_VALUE

    fun validatePriceValues() {
        if (minPriceNumber > maxPriceNumber) {
            minPrice = ""
            maxPrice = ""
        }
    }

    Box(
        modifier = Modifier
            .height(100.dp)
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
            // Champ pour le prix minimal
            PriceInputField(value = minPrice, onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    minPrice = it
                    validatePriceValues()
                }
            }, placeholder = "Min Price")

            // Champ pour le prix maximal
            PriceInputField(value = maxPrice, onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    maxPrice = it
                    validatePriceValues()
                }
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
            textStyle = androidx.compose.ui.text.TextStyle(
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
        placeholder = { Text(text = "1000", fontSize = 13.sp) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent, // Arrière-plan transparent
            unfocusedIndicatorColor = Color.Gray, // Couleur de la ligne lorsque le TextField n'est pas sélectionné
            focusedIndicatorColor = Color.Blue // Couleur de la ligne lorsque le TextField est sélectionné
        ),
        textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black, fontSize = 16.sp)
    )
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    FilterScreen()

}
