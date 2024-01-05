package com.openclassrooms.realestatemanager.utils

import com.openclassrooms.realestatemanager.data.repository.AgentRepository
import com.openclassrooms.realestatemanager.data.repository.PictureRepository
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository
import com.openclassrooms.realestatemanager.view.useCase.AddAgentUseCase
import com.openclassrooms.realestatemanager.view.useCase.AddPhotosUseCase
import com.openclassrooms.realestatemanager.view.useCase.AddPropertyUseCase
import com.openclassrooms.realestatemanager.view.useCase.GetAgentByIdUseCase
import com.openclassrooms.realestatemanager.view.useCase.GetAllPropertiesWithMainPictureUseCase
import com.openclassrooms.realestatemanager.view.useCase.GetPicturesForPropertyUseCase
import com.openclassrooms.realestatemanager.view.useCase.GetPropertyDetailsWithPictures
import com.openclassrooms.realestatemanager.view.useCase.UpdatePropertyUseCase
import com.openclassrooms.realestatemanager.view.useCase.UploadImageUseCase
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
    fun provideGetPicturesForPropertyUseCase(
        pictureRepository: PictureRepository
    ):
            GetPicturesForPropertyUseCase {
        return GetPicturesForPropertyUseCase(pictureRepository)
    }

    @Provides
    fun provideAddPropertyUseCase(
        propertyRepository: PropertyRepository
    ):
            AddPropertyUseCase {
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


    @Provides
    fun provideAddPhotosUseCase(pictureRepository: PictureRepository): AddPhotosUseCase {
        return AddPhotosUseCase(pictureRepository)
    }

    @Provides
    fun provideUploadImageUseCase(pictureRepository: PictureRepository): UploadImageUseCase {
        return UploadImageUseCase(pictureRepository)
    }





}