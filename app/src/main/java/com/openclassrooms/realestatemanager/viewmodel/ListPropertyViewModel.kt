package com.openclassrooms.realestatemanager.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PropertyModels
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PropertyWithMainPicture
import com.openclassrooms.realestatemanager.utils.Injection
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.view.useCase.GetAllPropertiesWithMainPictureUseCase
import com.openclassrooms.realestatemanager.view.useCase.SearchUseCase
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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

    init {
        // Lire l'état du switch de SharedPreferences
        val sharedPref = context.getSharedPreferences("CurrencyPreference", Context.MODE_PRIVATE)
        _isEuro.value = sharedPref.getBoolean("isEuro", true)

        // Ajouter propertiesWithMainPicture comme source pour _convertedProperties
        _convertedProperties.addSource(propertiesWithMainPicture) { properties ->
            if (properties != null) {
                _convertedProperties.value = convertPrices(properties, _isEuro.value ?: true)
            }
        }

        // Ajouter _isEuro comme source pour _convertedProperties
        _convertedProperties.addSource(_isEuro) { isEuro ->
            propertiesWithMainPicture.value?.let { properties ->
                _convertedProperties.value = convertPrices(properties, isEuro)
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

    private fun convertPrices(properties: List<PropertyWithMainPicture>, isEuro: Boolean): List<PropertyWithMainPicture> {
        return if (isEuro) {
            properties.map { it.copy(property = it.property.copy(price = Utils.convertDollarToEuro(it.property.price))) }
        } else {
            properties.map { it.copy(property = it.property.copy(price = Utils.convertEuroToDollar(it.property.price))) }
        }
    }

    fun updateCurrencyPreference(isEuro: Boolean) {
        _isEuro.value = isEuro
        // Potentiellement sauvegarder la préférence dans SharedPreferences ici si nécessaire
    }

}
