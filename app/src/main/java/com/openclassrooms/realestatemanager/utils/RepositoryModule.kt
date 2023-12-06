package com.openclassrooms.realestatemanager.utils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.google.firebase.firestore.FirebaseFirestore
import com.openclassrooms.realestatemanager.data.repository.AgentRepository
import com.openclassrooms.realestatemanager.data.repository.PictureRepository
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providePictureRepository(firestore: FirebaseFirestore): PictureRepository {
        return PictureRepository(firestore)
    }

    @Provides
    fun providePropertyRepository(firestore: FirebaseFirestore): PropertyRepository {
        return PropertyRepository(firestore)
    }

    @Provides
    fun provideAgentRepository(firestore: FirebaseFirestore): AgentRepository {
        return AgentRepository(firestore)
    }

    // Fournir FirebaseFirestore si nécessaire
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}
