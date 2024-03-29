package com.openclassrooms.realestatemanager.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PropertyWithMainPicture
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.view.useCase.GetAllPropertiesWithMainPictureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapPropertyViewModel @Inject constructor(
    private val getAllPropertiesWithMainPictureUseCase: GetAllPropertiesWithMainPictureUseCase,
    @ApplicationContext private val context: Context
)
    : ViewModel() {
    private val _properties = MutableLiveData<List<PropertyWithMainPicture>>()
    val properties: LiveData<List<PropertyWithMainPicture>> = _properties

    init {
        loadProperties()
    }

    private fun loadProperties() {
        viewModelScope.launch {
            val isInternetAvailable = Utils.isInternetAvailable(context)
            val propertyList = getAllPropertiesWithMainPictureUseCase(isInternetAvailable)
            _properties.postValue(propertyList)
        }
    }

}