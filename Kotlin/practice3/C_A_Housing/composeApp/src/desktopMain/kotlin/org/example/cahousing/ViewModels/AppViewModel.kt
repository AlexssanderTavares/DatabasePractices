package org.example.cahousing.ViewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    companion object {
        val INSTANCE: AppViewModel = AppViewModel()
    }

    private val displayDepartments: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val _displayDepartments: StateFlow<Boolean> = displayDepartments.asStateFlow()

    fun toggleDisplayDepartments(turnOn: Boolean) {
        displayDepartments.value = turnOn
        println("Toggling departments on: ${_displayDepartments.value}")
    }


}