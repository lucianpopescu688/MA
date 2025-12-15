package com.example.servicebuddy.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.servicebuddy.database.AppDatabase
import com.example.servicebuddy.model.MaintenanceEvent
import com.example.servicebuddy.repository.EventsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventViewModel(application: Application) : AndroidViewModel(application) {

    val repository = EventsRepository(AppDatabase.getDatabase(application).eventDao())
    val events = repository.allEvents
    val isOnline = repository.connectionStatus

    private val _snackbarMessage = MutableLiveData<String?>()
    val snackbarMessage: LiveData<String?> = _snackbarMessage

    init {
        refreshData()
    }

    fun onSnackbarMessageShown() {
        _snackbarMessage.value = null
    }

    fun refreshData() = viewModelScope.launch(Dispatchers.IO) {
        repository.refreshEvents()
    }

    fun addEvent(event: MaintenanceEvent) = viewModelScope.launch(Dispatchers.IO) {
        if (repository.createEvent(event)) {
            _snackbarMessage.postValue("Event Added: ${event.vehicleIdentifier}")
        }
    }

    fun updateEventById(oldId: String, event: MaintenanceEvent) = viewModelScope.launch(Dispatchers.IO) {
        if (repository.updateEvent(event)) {
            _snackbarMessage.postValue("Event Updated: ${event.vehicleIdentifier}")
        }
    }

    fun deleteEvent(id: String) = viewModelScope.launch(Dispatchers.IO) {
        if (repository.deleteEvent(id)) {
            _snackbarMessage.postValue("Event Deleted")
        }
    }

    suspend fun getEventByIdSuspend(id: String) = repository.getEvent(id)

    fun getEventById(id: String, onResult: (MaintenanceEvent?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val event = repository.getEvent(id)
            withContext(Dispatchers.Main) {
                onResult(event)
            }
        }
    }
}
