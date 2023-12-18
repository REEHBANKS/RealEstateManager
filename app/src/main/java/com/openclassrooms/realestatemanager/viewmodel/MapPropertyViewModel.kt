package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.models.PropertyWithMainPicture
import com.openclassrooms.realestatemanager.view.useCase.GetAllPropertiesWithMainPictureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapPropertyViewModel @Inject constructor(
    private val getAllPropertiesWithMainPictureUseCase: GetAllPropertiesWithMainPictureUseCase)
    : ViewModel() {
    private val _properties = MutableLiveData<List<PropertyWithMainPicture>>()
    val properties: LiveData<List<PropertyWithMainPicture>> = _properties

    init {
        loadProperties()
    }

    private fun loadProperties() {
        viewModelScope.launch {
            val propertyList = getAllPropertiesWithMainPictureUseCase()
            _properties.postValue(propertyList)
        }
    }

}