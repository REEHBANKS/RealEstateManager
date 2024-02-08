package com.openclassrooms.realestatemanager.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
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

    private val _propertiesWithPicturesFiltered = MutableLiveData<List<PropertyWithMainPicture>>()
    val propertiesWithPicturesFiltered: LiveData<List<PropertyWithMainPicture>> = _propertiesWithPicturesFiltered

    private val _isEuro = MutableLiveData<Boolean>().apply {
        val sharedPref = context.getSharedPreferences("CurrencyPreference", Context.MODE_PRIVATE)
        value = sharedPref.getBoolean("isEuro", false)
    }

    val isEuro: LiveData<Boolean> = _isEuro


    private val propertiesWithMainPicture: LiveData<List<PropertyWithMainPicture>> = liveData {
        val isInternetAvailable = Utils.isInternetAvailable(context)
        val properties = getAllPropertiesWithMainPictureUseCase(isInternetAvailable)
        emit(properties)
    }


    val convertedProperties: LiveData<List<PropertyWithMainPicture>> = propertiesWithMainPicture.switchMap { properties ->
        liveData {
            if (_isEuro.value == true) {
                emit(properties.map { it.convertToEuro() })
            } else {
                emit(properties)
            }
        }
    }

    private fun PropertyWithMainPicture.convertToEuro(): PropertyWithMainPicture =
        this.copy(property = this.property.copy(price = Utils.convertDollarToEuro(this.property.price)))

    private fun PropertyWithMainPicture.convertToDollar(): PropertyWithMainPicture =
        this.copy(property = this.property.copy(price = Utils.convertEuroToDollar(this.property.price)))

    init {
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

    fun updateCurrencyPreference(isEuro: Boolean) {
        val sharedPref = context.getSharedPreferences("CurrencyPreference", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isEuro", isEuro)
            apply()
        }

        _isEuro.value?.let {
            if (it != isEuro) {
                _isEuro.value = isEuro
                propertiesWithMainPicture.value?.let { properties ->
                    _propertiesWithPicturesFiltered.value = if (isEuro) {
                        properties.map { it.convertToEuro() }
                    } else {
                        properties.map { it.convertToDollar() }
                    }
                }
            }
        }
    }
}
