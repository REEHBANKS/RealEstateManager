package com.openclassrooms.realestatemanager.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PropertyWithMainPicture
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.view.useCase.GetAllPropertiesWithMainPictureUseCase
import com.openclassrooms.realestatemanager.view.useCase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ListPropertyViewModel @Inject constructor(
    @SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context,
    private val getAllPropertiesWithMainPictureUseCase: GetAllPropertiesWithMainPictureUseCase,
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _propertiesWithMainPicture = liveData(Dispatchers.IO) {
        emit(getAllPropertiesWithMainPictureUseCase())
    }
    private val propertiesWithMainPicture: LiveData<List<PropertyWithMainPicture>> = _propertiesWithMainPicture

    private val _propertiesWithPicturesFiltered = MutableLiveData<List<PropertyWithMainPicture>>()
    val propertiesWithPicturesFiltered: LiveData<List<PropertyWithMainPicture>> = _propertiesWithPicturesFiltered

    private val _isEuro = MutableLiveData<Boolean>()
    val isEuro: LiveData<Boolean> = _isEuro

    private val _convertedProperties = MediatorLiveData<List<PropertyWithMainPicture>>()
    val convertedProperties: LiveData<List<PropertyWithMainPicture>> = _convertedProperties

    private var originalCurrency: Currency

    init {
        val sharedPref = context.getSharedPreferences("CurrencyPreference", Context.MODE_PRIVATE)
        val isEuroPref = sharedPref.getBoolean("isEuro", true)
        _isEuro.value = isEuroPref
        Log.d("ListPropertyViewModel", "_isEuro mis à jour : $isEuroPref")

        originalCurrency = if (isEuroPref) Currency.EURO else Currency.DOLLAR
        Log.d("ListPropertyViewModel", "originalCurrency initialisé à : $originalCurrency")


        // Ajouter propertiesWithMainPicture comme source pour _convertedProperties
        _convertedProperties.addSource(propertiesWithMainPicture) { properties ->
            Log.d("ListPropertyViewModel", "propertiesWithMainPicture: ${properties.size} propriétés chargées")
            if (properties != null) {
                _convertedProperties.value = convertPricesIfNeeded(properties, _isEuro.value ?: true)
            }
        }

        // Ajouter _isEuro comme source pour _convertedProperties
        _convertedProperties.addSource(_isEuro) { isEuro ->
            Log.d("ListPropertyViewModel", "Changement de devise détecté: isEuro = $isEuro")
            propertiesWithMainPicture.value?.let { properties ->
                if (properties.isNotEmpty()) {
                    _convertedProperties.value = convertPricesIfNeeded(properties, isEuro)
                }
            }
        }

        // Réagir aux résultats de recherche
        viewModelScope.launch {
            searchUseCase.searchResults.collect { searchProperties ->
                if (searchProperties.isNotEmpty()) {
                    val allProperties = propertiesWithMainPicture.value ?: emptyList()
                    val filteredProperties = allProperties.filter { mainPictureProperty ->
                        searchProperties.any { it.id == mainPictureProperty.property.id }
                    }
                    _propertiesWithPicturesFiltered.postValue(filteredProperties)
                }
            }
        }
    }

    private fun convertPricesIfNeeded(properties: List<PropertyWithMainPicture>, isEuro: Boolean): List<PropertyWithMainPicture> {
        Log.d("ListPropertyViewModel", "convertPricesIfNeeded appelée avec isEuro = $isEuro et originalCurrency = $originalCurrency")
        return when {
            isEuro && originalCurrency == Currency.DOLLAR -> {
                Log.d("ListPropertyViewModel", "Conversion Dollar vers Euro")
                originalCurrency = Currency.EURO
                properties.map { it.copy(property = it.property.copy(price = Utils.convertDollarToEuro(it.property.price))) }
            }
            !isEuro && originalCurrency == Currency.EURO -> {
                Log.d("ListPropertyViewModel", "Conversion Euro vers Dollar")
                originalCurrency = Currency.DOLLAR
                properties.map { it.copy(property = it.property.copy(price = Utils.convertEuroToDollar(it.property.price))) }
            }
            else -> {
                Log.d("ListPropertyViewModel", "Pas de conversion nécessaire")
                properties
            }
        }
    }


    fun updateCurrencyPreference(isEuro: Boolean) {
        Log.d("ListPropertyViewModel", "updateCurrencyPreference appelée avec isEuro = $isEuro")

        if (_isEuro.value != isEuro) {
            Log.d("ListPropertyViewModel", "Changement détecté. Mise à jour de la préférence de devise.")

            _isEuro.value = isEuro
            saveCurrencyPreference(isEuro)

            val properties = propertiesWithMainPicture.value
            if (!properties.isNullOrEmpty()) {
                val convertedProperties = convertPricesIfNeeded(properties, isEuro)
                _convertedProperties.value = convertedProperties
                Log.d("ListPropertyViewModel", "Conversion effectuée : ${convertedProperties.size} propriétés converties.")
            } else {
                Log.d("ListPropertyViewModel", "Aucune propriété à convertir.")
            }
        } else {
            Log.d("ListPropertyViewModel", "Aucun changement dans la préférence de devise.")
        }
    }





    private fun saveCurrencyPreference(isEuro: Boolean) {
        val sharedPref = context.getSharedPreferences("CurrencyPreference", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isEuro", isEuro)
            apply()
        }
    }

}

enum class Currency {
    EURO, DOLLAR
}

