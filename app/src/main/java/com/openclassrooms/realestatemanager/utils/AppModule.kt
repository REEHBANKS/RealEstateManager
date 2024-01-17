package com.openclassrooms.realestatemanager.utils

import android.content.Context
import androidx.room.Room
import com.openclassrooms.realestatemanager.data.AppDatabase
import com.openclassrooms.realestatemanager.data.PhotoDao
import com.openclassrooms.realestatemanager.data.PropertyDao
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
import com.openclassrooms.realestatemanager.view.useCase.SearchUseCase
import com.openclassrooms.realestatemanager.view.useCase.UpdatePropertyUseCase
import com.openclassrooms.realestatemanager.view.useCase.UploadImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "real estate manager BDD "
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePropertyDao(database: AppDatabase): PropertyDao {
        return database.propertyDao()
    }

    @Provides
    fun providePhotoDao(database: AppDatabase): PhotoDao {
        return database.photoDao()
    }

    @Provides
    fun provideSearchUseCase(propertyRepository: PropertyRepository,
                             pictureRepository: PictureRepository):
            SearchUseCase {
        return SearchUseCase(propertyRepository, pictureRepository)
    }






}