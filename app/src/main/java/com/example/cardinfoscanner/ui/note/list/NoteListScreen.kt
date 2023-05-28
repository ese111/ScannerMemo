package com.example.cardinfoscanner.ui.note.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cardinfoscanner.R
import com.example.cardinfoscanner.stateholder.common.BaseUiState
import com.example.cardinfoscanner.stateholder.common.rememberUiState
import com.example.cardinfoscanner.data.local.model.Note
import com.example.cardinfoscanner.stateholder.note.list.NoteListState
import com.example.cardinfoscanner.stateholder.note.list.rememberNoteListState
import com.example.cardinfoscanner.ui.common.DropMenuState
import com.example.cardinfoscanner.ui.common.DropMenuTopAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoteListScreen(
    noteListState: NoteListState = rememberNoteListState()
) {
    Scaffold(
        topBar = {
            DropMenuTopAppBar(
                title = "Note",
                menuIcon = Icons.Filled.Add,
                dropMenuItems = listOf(
                    DropMenuState(
                        name = "직접 작성하기",
                        onClick = noteListState.moveToEdit
                    ),
                    DropMenuState(
                        name = "사진으로 추가하기",
                        onClick = noteListState.moveToCamera
                    )
                )
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = noteListState.snackBarHostState,
                modifier = Modifier.padding(16.dp)
            ) { data ->
                Snackbar(
                    action = {
                        TextButton(onClick = noteListState.onCancelRemove) {
                            data.visuals.actionLabel?.let { Text(text = it) }
                        }
                    }
                ) {
                    Text(text = data.visuals.message)
                }
            }
        }
    ) { paddingValues ->

        if(noteListState.noteListState.value.isEmpty()) {
            EmptyNoteItem(
                Modifier.padding(paddingValues = paddingValues),
                onClick = noteListState.moveToCamera
            )
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.padding(paddingValues = paddingValues),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            )
        ) {
            items(items = noteListState.noteListState.value, key = { note -> note.id }) { item ->
                NoteItem(
                    note = item,
                    removeNote = {
                        noteListState.onRemoveNote(item)
                        noteListState.uiState.scope.launch {
                            noteListState.snackBarHostState.showSnackbar(
                                message = "삭제 완료",
                                actionLabel = "실행 취소",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    onClickItem = { noteListState.onClickNote(item.id) }
                )
            }
        }
    }
}

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    removeNote: () -> Unit = {},
    onClickItem: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClickItem() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "delete",
                    modifier = Modifier
                        .width(16.dp)
                        .height(16.dp)
                        .clickable { removeNote() }
                )
            }
            Spacer(
                modifier = Modifier.height(5.dp)
            )
            Text(text = note.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1)
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            Text(text = note.content, fontSize = 14.sp, overflow = TextOverflow.Ellipsis, maxLines = 2)
            Spacer(
                modifier = Modifier.height(15.dp)
            )
            Text(text = note.date, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun EmptyNoteItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .clickable {
                    onClick()
                },
            painter = painterResource(id = R.drawable.ic_baseline_queue_24),
            contentDescription = "",
            tint = Color.DarkGray
        )
        Text(text = "New Note!!")
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteScreenPreview() {
    val list = listOf(
        Note(
            0,
            "description1",
            "A module can be installed in multiple components. For example, maybe you have a binding in ViewComponent and ViewWithFragmentComponent and do not want to duplicate modules. @InstallIn({ViewComponent.class, ViewWithFragmentComponent.class}) will install a module in both components.",
            "22.04.24"
        ),
        Note(
            1,
            "description2",
            "A module can be installed in multiple components. For example, maybe you have a binding in ViewComponent and ViewWithFragmentComponent and do not want to duplicate modules. @InstallIn({ViewComponent.class, ViewWithFragmentComponent.class}) will install a module in both components.",
            "22.04.24"
        ),
        Note(
            2,
            "description3",
            "A module can be installed in multiple components. For example, maybe you have a binding in ViewComponent and ViewWithFragmentComponent and do not want to duplicate modules. @InstallIn({ViewComponent.class, ViewWithFragmentComponent.class}) will install a module in both components.",
            "22.04.24"
        ),
        Note(
            3,
            "description4",
            "A module can be installed in multiple components. For example, maybe you have a binding in ViewComponent and ViewWithFragmentComponent and do not want to duplicate modules. @InstallIn({ViewComponent.class, ViewWithFragmentComponent.class}) will install a module in both components.",
            "22.04.24"
        )
    )
    NoteListScreen()
}

@Preview(showBackground = true)
@Composable
private fun NoteItemPreview() {
    NoteItem(
        note = Note(0, "인공눈물 설명서", "하루에 1번 뚜껑을 따서...", "2023.03.03"),
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyNoteItemPreview() {
    EmptyNoteItem()
}
