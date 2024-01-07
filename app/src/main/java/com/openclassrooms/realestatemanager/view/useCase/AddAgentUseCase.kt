package com.openclassrooms.realestatemanager.view.useCase

import com.openclassrooms.realestatemanager.data.repository.AgentRepository

class AddAgentUseCase(private val agentRepository: AgentRepository) {

    fun execute(){
        agentRepository.createUser()
    }
}
