package com.openclassrooms.realestatemanager.viewmodel

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PropertyModels
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.view.useCase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor (
    private val searchUseCase: SearchUseCase,
    @ApplicationContext private val context: Context
) : ViewModel(

) {

    private val _properties = MutableLiveData<List<PropertyModels>>()
    val properties: LiveData<List<PropertyModels>> = _properties

    @RequiresApi(Build.VERSION_CODES.O)

    fun performSearch(
        selectedOptionType: String?,
        minSurface: Int?,
        maxSurface: Int?,
        minPrice: Int?,
        maxPrice: Int?,
        proximity: Set<String>,
        date: LocalDate?,
        filterByPhotoCount: Boolean,
        neighborhood: String?
    ) {
        Log.d("SearchViewModel", "performSearch called with params: ...")
        viewModelScope.launch {
            val results = searchUseCase.execute(
                selectedOptionType,
                minSurface ?: 0, // Utiliser 0 si minSurface est null
                maxSurface ?: Int.MAX_VALUE, // Utiliser Int.MAX_VALUE si maxSurface est null
                minPrice ?: 0, // Utiliser 0 si minPrice est null
                maxPrice ?: Int.MAX_VALUE, // Utiliser Int.MAX_VALUE si maxPrice est null
                proximity,
                date,
                filterByPhotoCount,
                neighborhood,
                Utils.isInternetAvailable(context)
            )
            _properties.postValue(results)
        }
    }

}