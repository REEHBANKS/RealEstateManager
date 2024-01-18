package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PropertyModels
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PropertyWithMainPicture
import com.openclassrooms.realestatemanager.view.useCase.GetAllPropertiesWithMainPictureUseCase
import com.openclassrooms.realestatemanager.view.useCase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ListPropertyViewModel @Inject constructor(
    private val getAllPropertiesWithMainPictureUseCase: GetAllPropertiesWithMainPictureUseCase,
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    val propertiesWithMainPicture = liveData(Dispatchers.IO) {
        emit(getAllPropertiesWithMainPictureUseCase())
    }

    private val _propertiesWithPicturesFiltered = MutableLiveData<List<PropertyWithMainPicture>>()
    val propertiesWithPicturesFiltered: LiveData<List<PropertyWithMainPicture>> = _propertiesWithPicturesFiltered

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
}
