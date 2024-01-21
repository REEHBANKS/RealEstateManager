package com.openclassrooms.realestatemanager.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth



import com.openclassrooms.realestatemanager.R


class SettingsActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            androidx.compose.material.MaterialTheme {
                FilterScreenSettings()
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FilterScreenSettings() {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("CurrencyPreference", Context.MODE_PRIVATE)

    // Lire l'état actuel de la devise à partir des SharedPreferences
    val savedIsEuro = sharedPref.getBoolean("isEuro", true)
    var isEuro by remember { mutableStateOf(savedIsEuro) }
    var showDialog by remember { mutableStateOf(true) }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = { Text("Settings") },
            text = {
                Column {
                    SwitchSettingItem(
                        title = "Currency",
                        subtitle = if (isEuro) "Euro" else "Dollar",
                        isChecked = isEuro,
                        onCheckedChange = { isEuro = it
                            saveCurrencyPreference(it, context)
                        },
                        iconId = R.drawable.euro_to_dollar
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                }
            },
            confirmButton = {

                Button(
                    onClick = {
                        // Ajoutez ici la logique de déconnexion de Firebase
                        FirebaseAuth.getInstance().signOut()
                        showDialog = false
                        // Vous pourriez également naviguer vers l'écran de connexion ici
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                    modifier = Modifier
                        .padding(end = 120.dp)
                ) {
                    Text("Déconnexion", color = Color.White)
                }
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }

            }
        )
    }
}

@Composable
fun SwitchSettingItem(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    iconId: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .padding(end = 8.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold)
            Text(text = subtitle)
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

fun saveCurrencyPreference(isEuro: Boolean, context: Context) {
    val sharedPref = context.getSharedPreferences("CurrencyPreference", Context.MODE_PRIVATE) ?: return
    with(sharedPref.edit()) {
        putBoolean("isEuro", isEuro)
        apply()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreviewSettings() {
    FilterScreenSettings()
}
