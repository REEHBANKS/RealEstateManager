package com.openclassrooms.realestatemanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.models.PropertyModels
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository
import com.openclassrooms.realestatemanager.view.useCase.AddPropertyUseCase
import com.openclassrooms.realestatemanager.view.useCase.GetAllPropertiesWithMainPictureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ListPropertyViewModel @Inject constructor(

    private val getAllPropertiesWithMainPictureUseCase: GetAllPropertiesWithMainPictureUseCase
) : ViewModel() {

    val propertiesWithMainPicture = liveData(Dispatchers.IO) {
        val properties = getAllPropertiesWithMainPictureUseCase()
        emit(properties)
    }




}
