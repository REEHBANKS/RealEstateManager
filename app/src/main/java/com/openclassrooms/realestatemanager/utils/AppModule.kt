package com.openclassrooms.realestatemanager.utils

import com.openclassrooms.realestatemanager.data.repository.AgentRepository
import com.openclassrooms.realestatemanager.data.repository.PictureRepository
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository
import com.openclassrooms.realestatemanager.useCase.AddAgentUseCase
import com.openclassrooms.realestatemanager.useCase.AddPropertyUseCase
import com.openclassrooms.realestatemanager.useCase.GetAgentByIdUseCase
import com.openclassrooms.realestatemanager.useCase.GetAllPropertiesWithMainPictureUseCase
import com.openclassrooms.realestatemanager.useCase.GetPropertyDetailsWithPictures
import com.openclassrooms.realestatemanager.useCase.UpdatePropertyUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideGetAllPropertiesWithMainPictureUseCase(
        propertyRepository: PropertyRepository,
        pictureRepository: PictureRepository
    ): GetAllPropertiesWithMainPictureUseCase {
        return GetAllPropertiesWithMainPictureUseCase(propertyRepository, pictureRepository)
    }

    @Provides
    fun provideAddPropertyUseCase(propertyRepository: PropertyRepository): AddPropertyUseCase {
        return AddPropertyUseCase(propertyRepository)
    }


    @Provides
    fun provideGetPropertyDetailsWithPicturesUseCase(
        propertyRepository: PropertyRepository,
        pictureRepository: PictureRepository
    ): GetPropertyDetailsWithPictures {
        return GetPropertyDetailsWithPictures(propertyRepository, pictureRepository)
    }

    @Provides
    fun provideUpdatePropertyUseCase(propertyRepository: PropertyRepository): UpdatePropertyUseCase {
        return UpdatePropertyUseCase(propertyRepository)
    }

    @Provides
    fun provideGetAgentByIdUseCase(agentRepository: AgentRepository): GetAgentByIdUseCase {
        return GetAgentByIdUseCase(agentRepository)
    }

    @Provides
    fun provideAddAgentUseCase(agentRepository: AgentRepository): AddAgentUseCase {
        return AddAgentUseCase(agentRepository)
    }


}