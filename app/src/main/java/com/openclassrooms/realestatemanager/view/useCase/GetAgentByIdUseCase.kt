package com.openclassrooms.realestatemanager.view.useCase

import com.openclassrooms.realestatemanager.data.models.AgentModels
import com.openclassrooms.realestatemanager.data.repository.AgentRepository

class GetAgentByIdUseCase(private val agentRepository: AgentRepository) {

    suspend operator fun invoke(agentId: String): AgentModels? {
        return agentRepository.getAgentById(agentId)
    }
}
