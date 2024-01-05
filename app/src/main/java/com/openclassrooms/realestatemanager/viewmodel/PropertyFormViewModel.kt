package com.openclassrooms.realestatemanager.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.models.PhotoDescription
import com.openclassrooms.realestatemanager.data.models.PropertyModels
import com.openclassrooms.realestatemanager.view.useCase.AddPhotosUseCase
import com.openclassrooms.realestatemanager.view.useCase.AddPropertyUseCase
import com.openclassrooms.realestatemanager.view.useCase.UploadImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject
@HiltViewModel
class PropertyFormViewModel @Inject constructor (
    private val addPropertyUseCase : AddPropertyUseCase,
    private val addPhotosUseCase: AddPhotosUseCase,
    private val uploadImageUseCase: UploadImageUseCase
)
            : ViewModel() {

    private val _operationStatus = MutableLiveData<OperationStatus>()
    val operationStatus: LiveData<OperationStatus> = _operationStatus

    // LiveData pour l'URL de l'image téléchargée
    private val _imageUploadLiveData = MutableLiveData<String>()
    val imageUploadLiveData: LiveData<String> = _imageUploadLiveData

    fun submitPropertyAndPhotos(property: PropertyModels, photos: List<PhotoDescription>) {
        viewModelScope.launch {
            val propertyId = addPropertyUseCase.addPropertyUseCase(property)
            if (propertyId != null) {
                val updatedPhotos = photos.map { it.copy(propertyId = propertyId) }
                val photosAddedSuccessfully = addPhotosUseCase.addPhotos(updatedPhotos)
                if (photosAddedSuccessfully) {
                    _operationStatus.postValue(OperationStatus.SUCCESS)
                } else {
                    _operationStatus.postValue(OperationStatus.PHOTOS_FAILURE)
                }
            } else {
                _operationStatus.postValue(OperationStatus.PROPERTY_FAILURE)
            }
        }
    }

    enum class OperationStatus {
        SUCCESS,
        PROPERTY_FAILURE,
        PHOTOS_FAILURE
    }

    fun uploadImage(inputStream: InputStream) {
        viewModelScope.launch {
            val downloadUrl = uploadImageUseCase.execute(inputStream)
            if (downloadUrl != null) {
                _imageUploadLiveData.postValue(downloadUrl.toString())
            } else {
                // Gérer l'échec de l'upload
            }
        }
    }


}