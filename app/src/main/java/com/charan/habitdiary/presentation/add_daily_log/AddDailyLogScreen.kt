package com.charan.habitdiary.presentation.add_daily_log

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charan.habitdiary.presentation.add_daily_log.components.AddImageItem
import com.charan.habitdiary.presentation.add_daily_log.components.AddNoteItem
import com.charan.habitdiary.presentation.add_daily_log.components.DateTimeRow
import com.charan.habitdiary.presentation.add_daily_log.components.HabitDetailsCard
import com.charan.habitdiary.presentation.add_daily_log.components.ImagePickOptionsBottomSheet
import com.charan.habitdiary.presentation.common.components.ActionButtonRow
import com.charan.habitdiary.presentation.common.components.DeleteWarningDialog
import com.charan.habitdiary.presentation.common.components.RationaleDialog
import com.charan.habitdiary.presentation.common.components.SelectDateDialog
import com.charan.habitdiary.presentation.common.components.SelectTimeDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun AddDailyLogScreen(
    onNavigateBack : () -> Unit,
    logId : Int? = null
) {

    val viewModel = hiltViewModel<DailyLogViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val imagePickOptionsBottomSheetState = rememberModalBottomSheetState()
    LaunchedEffect(Unit) {
        viewModel.onEvent(DailyLogEvent.InitializeLog(logId))
    }
    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri->
        if(uri!=null){
            viewModel.onEvent(DailyLogEvent.OnImagePick(uri))
        }

    }
    val captureImage = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success->
        if(success){
            viewModel.onEvent(DailyLogEvent.OnImagePick(state.tempImagePath.toUri()))
        }
    }
    val cameraPermission = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    ) {
        if(it){
            viewModel.onEvent(DailyLogEvent.OnTakePhotoClick)
        }

    }

    if(state.showRationaleForCameraPermission){
        RationaleDialog(
            title = "Camera Permission Required",
            message = "This app requires camera access to take photos for your daily journal entries.",
            onDismissRequest = {
                viewModel.onEvent(DailyLogEvent.ToggleShowRationaleForCameraPermission(false))
            },
            onConfirmRequest = {
                viewModel.onEvent(DailyLogEvent.OnOpenSettingsForPermissions)
                viewModel.onEvent(DailyLogEvent.ToggleShowRationaleForCameraPermission(false))


            }
        )
    }

    if(state.showDateSelectDialog){
        SelectDateDialog(
            onDismissRequest = {
                viewModel.onEvent(DailyLogEvent.OnToggleDateSelectorDialog(false))
            },
            onDateSelected = {
                viewModel.onEvent(DailyLogEvent.OnDateSelected(it ?: 0L))
            },
            dateMillis = state.dailyLogItemDetails.date,


        )
    }

    if(state.showTimeSelectDialog){
        SelectTimeDialog(
            onDismiss = {
                viewModel.onEvent(DailyLogEvent.OnToggleTimeSelectorDialog(false))
            },
            onTimeSelected = {
                viewModel.onEvent(DailyLogEvent.OnTimeSelected(it))
            },
            selectedTime = state.dailyLogItemDetails.time,
            is24HourFormat = state.is24HourFormat
        )
    }


    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when(it){
                DailyLogEffect.OnNavigateBack -> {
                    onNavigateBack()

                }
                DailyLogEffect.OnOpenImagePicker -> {
                    pickImage.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )

                }

                is DailyLogEffect.OnTakePhoto -> {
                    captureImage.launch(state.tempImagePath.toUri())
                }

                DailyLogEffect.OnRequestCameraPermission -> {
                    if(cameraPermission.status.shouldShowRationale){
                        viewModel.onEvent(DailyLogEvent.ToggleShowRationaleForCameraPermission(true))
                    } else {
                        cameraPermission.launchPermissionRequest()
                    }

                }
            }
        }
    }

    if(state.showImagePickOptionsSheet){
        ImagePickOptionsBottomSheet(
            onImageFromGalleryClick = {
                viewModel.onEvent(DailyLogEvent.OnPickFromGalleryClick)

            },
            onImageFromCameraClick = {
                viewModel.onEvent(DailyLogEvent.OnTakePhotoClick)

            },
            onDismissRequest = {
                viewModel.onEvent(DailyLogEvent.OnToggleImagePickOptionsSheet(false))
            },
            sheetState = imagePickOptionsBottomSheetState
        )


    }

    if(state.showDeleteDialog){
        DeleteWarningDialog(
            onConfirm = {
                viewModel.onEvent(DailyLogEvent.OnDeleteDailyLog)
            },
            onDismiss = {
                viewModel.onEvent(DailyLogEvent.OnToggleDeleteDialog(false))
            }
        )
    }
    Scaffold(
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text("Add Daily Journal")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(DailyLogEvent.OnBackClick)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            ActionButtonRow(
                saveButtonText = "Save Log",
                showDeleteButton = state.isEdit,
                onSave = {
                    viewModel.onEvent(DailyLogEvent.OnSaveDailyLogClick)
                },
                onDelete = {
                    viewModel.onEvent(DailyLogEvent.OnToggleDeleteDialog(true))
                },
                isSaveEnabled = state.dailyLogItemDetails.imagePath.isNotEmpty() || state.dailyLogItemDetails.notesText.isNotEmpty()
            )
        }
    ) { innerPadding->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if(state.dailyLogItemDetails.habitId!=null){
                item {
                    HabitDetailsCard(
                        habitTitle = state.dailyLogItemDetails.habitName ?: "",

                    )
                }
            }
            item {
                AddImageItem(
                    imageURL = state.dailyLogItemDetails.imagePath,
                    onClick = {
                        viewModel.onEvent(DailyLogEvent.OnToggleImagePickOptionsSheet(true))
                    }


                )
                AddNoteItem(
                    value = state.dailyLogItemDetails.notesText,
                    onValueChange = {
                        viewModel.onEvent(DailyLogEvent.OnNotesTextChange(it))
                    }
                )
                DateTimeRow(
                    date = state.dailyLogItemDetails.formattedDateString,
                    time = state.dailyLogItemDetails.formattedTimeString,
                    onDateClick = {
                        viewModel.onEvent(DailyLogEvent.OnToggleDateSelectorDialog(true))
                    },
                    onTimeClick = {
                        viewModel.onEvent(DailyLogEvent.OnToggleTimeSelectorDialog(true))
                    }
                )

            }


        }

    }

}

@Preview
@Composable
fun AddDailyLogScreenPreview() {
    AddDailyLogScreen(
        onNavigateBack = {}

    )
}