package com.example.cardinfoscanner.ui.note.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cardinfoscanner.data.local.model.Note
import com.example.cardinfoscanner.stateholder.common.BaseUiState
import com.example.cardinfoscanner.stateholder.common.rememberUiState
import com.example.cardinfoscanner.stateholder.note.detail.NoteDetailState
import com.example.cardinfoscanner.stateholder.note.detail.rememberNoteDetailState
import com.example.cardinfoscanner.ui.common.DropMenuState
import com.example.cardinfoscanner.ui.common.DropMenuTopAppBar
import com.example.cardinfoscanner.ui.common.NormalDialog
import com.example.cardinfoscanner.ui.note.edit.NoteEditorView
import kotlinx.coroutines.launch

sealed interface NoteDetailUiState {
    object Loading : NoteDetailUiState
    data class Success(val data: Note) : NoteDetailUiState
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoteDetailScreen(
    uiState: BaseUiState = rememberUiState(),
    noteState: NoteDetailState = rememberNoteDetailState(),
    removeNote:() -> Unit = {},
    saveNote:() -> Unit = {},
    isShowRemoveDialog: (Boolean) -> Unit = {},
    isShowSaveDialog: (Boolean) -> Unit = {},
    onClickUpButton: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onContentChange: (String) -> Unit = {},
    onTitleFocusChanged: (Boolean) -> Unit = {},
    onContentFocusChanged: (Boolean) -> Unit = {}
) {

    BackHandler(enabled = noteState.isVisible.value) {
        uiState.scope.launch {
            uiState.focusManager.clearFocus()
        }
    }

    if(noteState.saveDialogState.value) {
        NormalDialog(
            title = "변경 사항을 저장하시겠습니까?",
            phrase = "변경된 데이터는 복구되지 않습니다.",
            confirmText = "확인",
            dismissText = "취소",
            onConfirm = saveNote,
            onDismiss = { isShowSaveDialog(false) }
        )
    }
    if(noteState.removeDialogState.value) {
        NormalDialog(
            title = "정말로 삭제하시겠습니까?",
            phrase = "삭제된 데이터는 복구되지 않습니다.",
            confirmText = "확인",
            dismissText = "취소",
            onConfirm = removeNote,
            onDismiss = { isShowRemoveDialog(false) }
        )
    }

    Scaffold(
        topBar = {
            DropMenuTopAppBar(
                title = "NoteDetail",
                backButtonVisible = true,
                menuIcon = Icons.Default.Menu,
                onClickBackButton = onClickUpButton,
                dropMenuItems = listOf(
                    DropMenuState(
                        name = "저장하기",
                        onClick = { isShowSaveDialog(true) }
                    ),
                    DropMenuState(
                        name = "삭제하기",
                        onClick = { isShowRemoveDialog(true) }
                    )
                )
            )
        }
    ) { paddingValues ->
        NoteEditorView(
            modifier = Modifier.padding(paddingValues),
            title = noteState.titleFieldState.value.value,
            content = noteState.contentFieldState.value.value,
            onTitleChange = onTitleChange,
            onContentChange = onContentChange,
            onTitleFocusChanged = onTitleFocusChanged,
            onContentFocusChanged = onContentFocusChanged
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun NoteDetailPreView() {
    NoteDetailScreen()
}

@Preview(showBackground = true)
@Composable
private fun NoteDetailContentPreView() {}
