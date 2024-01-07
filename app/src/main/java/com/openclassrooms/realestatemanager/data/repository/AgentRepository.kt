package com.openclassrooms.realestatemanager.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.openclassrooms.realestatemanager.data.models.modelFirebase.AgentModel
import kotlinx.coroutines.tasks.await

class AgentRepository(private val firestore: FirebaseFirestore) {

    fun createUser() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val uid = user.uid
            FirebaseFirestore.getInstance().collection("agents").document(uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (!documentSnapshot.exists()) {
                        val username = user.displayName ?: ""
                        val userEmail = user.email ?: ""
                        val agentToCreate = AgentModel(uid, userEmail, username)
                        FirebaseFirestore.getInstance().collection("agents").document(uid).set(agentToCreate)
                    }
                }
                .addOnFailureListener { e ->
                    // Error handling for agent creation failure.
                }
        }
    }

    suspend fun getAgentById(agentId: String): AgentModel? {
        val documentSnapshot = firestore.collection("agents").document(agentId).get().await()
        return documentSnapshot.toObject<AgentModel>()
    }
}
