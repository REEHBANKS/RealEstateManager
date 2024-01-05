package com.openclassrooms.realestatemanager.view.useCase

import android.net.Uri
import com.openclassrooms.realestatemanager.data.repository.PictureRepository
import java.io.InputStream

class UploadImageUseCase(private val pictureRepository: PictureRepository) {
    suspend fun execute(inputStream: InputStream): Uri? {
        return pictureRepository.uploadImageAndGetDownloadUrl(inputStream)
    }
}