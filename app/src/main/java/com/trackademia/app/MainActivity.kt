package com.trackademia.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.trackademia.app.ui.theme.TrackademiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrackademiaTheme {
                TrackademiaApp()
            }
        }
    }
}

@Composable
fun TrackademiaApp() {
    // 1. Get the Application context using LocalContext
    val appContext = LocalContext.current.applicationContext as android.app.Application

    // 2. Initialize the ViewModel inside the Composable body
    val viewModel: NoteViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                NoteViewModel(appContext)
            }
        }
    )

    // Navigation state: null means List screen, a Note object means Editor screen
    var currentScreen by remember { mutableStateOf<Screen>(Screen.NotesList) }

    when (currentScreen) {
        is Screen.NotesList -> {
            NotesListScreen(
                viewModel = viewModel,
                onNavigateToEditor = { currentScreen = Screen.Editor(note = null) },
                onNavigateToEditNote = { note -> currentScreen = Screen.Editor(note = note) }
            )
        }
        is Screen.Editor -> {
            val noteToEdit = (currentScreen as Screen.Editor).note
            NoteEditorScreen(
                initialNote = noteToEdit,
                onSaveNote = { title, content, parsedDate ->
                    if (noteToEdit == null) {
                        // Create new note
                        viewModel.insertNote(Note(
                            title = title,
                            content = content,
                            parsedDate = parsedDate,
                            hasReminder = parsedDate != null
                        ))
                    } else {
                        // Update existing note
                        viewModel.updateNote(noteToEdit.copy(
                            title = title,
                            content = content,
                            parsedDate = parsedDate,
                            hasReminder = parsedDate != null
                        ))
                    }
                    currentScreen = Screen.NotesList
                },
                onBack = { currentScreen = Screen.NotesList }
            )
        }
    }
}

// Simple sealed class for navigation
sealed class Screen {
    object NotesList : Screen()
    data class Editor(val note: Note?) : Screen()
}