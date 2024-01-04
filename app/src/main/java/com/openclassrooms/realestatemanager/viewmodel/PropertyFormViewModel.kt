package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.models.PropertyModels
import com.openclassrooms.realestatemanager.view.useCase.AddPropertyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class PropertyFormViewModel @Inject constructor (
    private val addPropertyUseCase : AddPropertyUseCase)
            : ViewModel() {

    private val _addPropertyResult = MutableLiveData<Boolean>()
    val addPropertyResult: LiveData<Boolean> get() = _addPropertyResult

    fun addProperty(property: PropertyModels) {
        viewModelScope.launch {
            val result = addPropertyUseCase(property)
            _addPropertyResult.postValue(result)
        }
    }



}