package com.openclassrooms.realestatemanager.view.useCase

import com.openclassrooms.realestatemanager.data.models.modelFirebase.AgentModel
import com.openclassrooms.realestatemanager.data.repository.AgentRepository

class GetAgentByIdUseCase(private val agentRepository: AgentRepository) {

    suspend operator fun invoke(agentId: String): AgentModel? {
        return agentRepository.getAgentById(agentId)
    }
}
