package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.models.modelFirebase.AgentModel
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PhotoDescription
import com.openclassrooms.realestatemanager.view.useCase.GetAgentByIdUseCase
import com.openclassrooms.realestatemanager.view.useCase.GetPicturesForPropertyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyDetailViewModel @Inject constructor(
    private val getPicturesForPropertyUseCase: GetPicturesForPropertyUseCase,
    private val getAgentByIdUseCase: GetAgentByIdUseCase
) : ViewModel() {

    private val _picturesLiveData = MutableLiveData<List<PhotoDescription>>()
    val picturesLiveData: LiveData<List<PhotoDescription>> = _picturesLiveData

    private val _agentLiveData = MutableLiveData<AgentModel?>()
    val agentLiveData: LiveData<AgentModel?> = _agentLiveData

    fun getPicturesForProperty(propertyId: String) {
        viewModelScope.launch {
            val pictures = getPicturesForPropertyUseCase.execute(propertyId)
            _picturesLiveData.postValue(pictures)

        }
    }

    fun getOneAgentWithId(agentId: String) {
        viewModelScope.launch {
            val agent = getAgentByIdUseCase.invoke(agentId)
            _agentLiveData.postValue(agent)
        }
    }
}


