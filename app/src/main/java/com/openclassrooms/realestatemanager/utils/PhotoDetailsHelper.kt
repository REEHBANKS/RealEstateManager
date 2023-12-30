package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog

class PhotoDetailsHelper(private val context: Context)  {

    fun promptForPhotoCaption(context: Context, callback: (String, Boolean) -> Unit) {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(50, 30, 50, 30)
        }

        val editText = EditText(context).apply {
            hint = "Enter caption"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val checkBox = CheckBox(context).apply {
            text = "Set as primary photo"
            isChecked = false // Default value
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        layout.addView(editText)
        layout.addView(checkBox)

        AlertDialog.Builder(context)
            .setTitle("Add Photo Details")
            .setView(layout)
            .setPositiveButton("OK") { dialog, _ ->
                val caption = editText.text.toString()
                val isPrimary = checkBox.isChecked
                // Utiliser le callback pour retourner la légende et l'état de la photo principale
                callback(caption, isPrimary)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }

}