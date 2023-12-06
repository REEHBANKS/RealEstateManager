package com.openclassrooms.realestatemanager.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.openclassrooms.realestatemanager.data.models.AgentModels
import kotlinx.coroutines.tasks.await

class AgentRepository(private val firestore: FirebaseFirestore) {

    suspend fun addAgent(agent: AgentModels): Boolean {
        return try {
            val newAgentRef = firestore.collection("agents").document()
            val newAgentId = newAgentRef.id
            val newAgent = agent.copy(agentId = newAgentId)
            newAgentRef.set(newAgent).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAgentById(agentId: String): AgentModels? {
        val documentSnapshot = firestore.collection("agents").document(agentId).get().await()
        return documentSnapshot.toObject<AgentModels>()
    }
}
