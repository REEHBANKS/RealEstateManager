package com.openclassrooms.realestatemanager.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.models.PhotoDescription
import com.openclassrooms.realestatemanager.view.useCase.GetPicturesForPropertyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyDetailViewModel @Inject constructor(
    private val getPicturesForPropertyUseCase: GetPicturesForPropertyUseCase
) : ViewModel() {

    private val _picturesLiveData = MutableLiveData<List<PhotoDescription>>()
    val picturesLiveData: LiveData<List<PhotoDescription>> = _picturesLiveData

    fun getPicturesForProperty(propertyId: String) {
        viewModelScope.launch {
            val pictures = getPicturesForPropertyUseCase.execute(propertyId)
            _picturesLiveData.postValue(pictures)

            // Loguer les identifiants des photos pour vérification
            pictures.forEach { picture ->
                Log.d("PropertyDetailViewModel", "Photo ID: ${picture.description}")
            }
        }
    }
}

