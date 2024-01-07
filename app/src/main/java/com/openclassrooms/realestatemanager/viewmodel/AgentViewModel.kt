package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.view.useCase.AddAgentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class AgentViewModel @Inject constructor(
    private val addAgentUseCase: AddAgentUseCase
) : ViewModel() {

    private val _addAgentStatus = MutableLiveData<AddAgentStatus>()
    val addAgentStatus: LiveData<AddAgentStatus> = _addAgentStatus

    fun createUser(){
        addAgentUseCase.execute()
    }
    sealed class AddAgentStatus {
        object Loading : AddAgentStatus()
        object Success : AddAgentStatus()
        data class Error(val message: String) : AddAgentStatus()
    }
}
