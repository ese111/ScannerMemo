package com.example.cardinfoscanner.stateholder.note

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.cardinfoscanner.stateholder.common.BaseUiState
import com.example.cardinfoscanner.stateholder.common.rememberUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

@Stable
class NoteListState(
    val uiState: BaseUiState,
    val noteList: State<List<Note>>,
    val setData: (Note) -> Unit
)

@Serializable
data class Note(
    val id: Long = -1,
    val title: String,
    val content: String,
    val date: String
): java.io.Serializable

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun rememberNoteListState(
    uiState: BaseUiState = rememberUiState(),
    noteList: State<List<Note>> = remember { mutableStateOf(emptyList()) },
    setData: (Note) -> Unit
) = remember(noteList) {
    NoteListState(
        uiState = uiState,
        noteList = noteList,
        setData = setData
    )
}