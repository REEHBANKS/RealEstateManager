package com.openclassrooms.realestatemanager.utils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.google.firebase.firestore.FirebaseFirestore
import com.openclassrooms.realestatemanager.data.PhotoDao
import com.openclassrooms.realestatemanager.data.PropertyDao
import com.openclassrooms.realestatemanager.data.repository.AgentRepository
import com.openclassrooms.realestatemanager.data.repository.PictureRepository
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providePictureRepository(
        firestore: FirebaseFirestore,
        photoDao: PhotoDao
    ): PictureRepository {
        return PictureRepository(firestore, photoDao)
    }

    @Provides
    fun providePropertyRepository(
        firestore: FirebaseFirestore,
        propertyDao: PropertyDao
    ): PropertyRepository {
        return PropertyRepository(firestore, propertyDao)
    }


    @Provides
    fun provideAgentRepository(firestore: FirebaseFirestore): AgentRepository {
        return AgentRepository(firestore)
    }


    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}
