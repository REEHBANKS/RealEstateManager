package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.openclassrooms.realestatemanager.view.useCase.GetAllPropertiesWithMainPictureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
