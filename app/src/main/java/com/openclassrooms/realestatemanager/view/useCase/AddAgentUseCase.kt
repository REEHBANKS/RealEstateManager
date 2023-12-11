package com.openclassrooms.realestatemanager.view.useCase

import com.openclassrooms.realestatemanager.data.models.AgentModels
import com.openclassrooms.realestatemanager.data.repository.AgentRepository
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository

class AddAgentUseCase(private val agentRepository: AgentRepository) {

    suspend operator fun invoke(agent: AgentModels): Boolean {
        return agentRepository.addAgent(agent)
    }
}
