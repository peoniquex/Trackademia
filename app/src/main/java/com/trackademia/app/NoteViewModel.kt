package com.trackademia.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = NoteDatabase.getDatabase(application).noteDao()

    // Expose the notes as a StateFlow for the Compose UI to observe
    val notes: StateFlow<List<Note>> = dao.getAllNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // CRUD Operations
    fun insertNote(note: Note) {
        viewModelScope.launch {
            dao.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            dao.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            dao.deleteNote(note)
        }
    }
}