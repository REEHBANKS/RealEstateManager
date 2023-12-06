package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.openclassrooms.realestatemanager.useCase.GetAllPropertiesWithMainPictureUseCase
import kotlinx.coroutines.Dispatchers


class ListRealEstatePropertyViewModel(
    private val getAllPropertiesWithMainPictureUseCase: GetAllPropertiesWithMainPictureUseCase
) : ViewModel() {

    val propertiesWithMainPicture = liveData(Dispatchers.IO) {
        val properties = getAllPropertiesWithMainPictureUseCase()
        emit(properties)
    }
}
