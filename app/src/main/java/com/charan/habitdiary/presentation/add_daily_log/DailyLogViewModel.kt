package com.charan.habitdiary.presentation.add_daily_log

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charan.habitdiary.data.repository.DataStoreRepository
import com.charan.habitdiary.data.repository.FileRepository
import com.charan.habitdiary.data.repository.HabitLocalRepository
import com.charan.habitdiary.presentation.mapper.toDailyLogEntity
import com.charan.habitdiary.presentation.mapper.toDailyLogItemDetails
import com.charan.habitdiary.utils.DateUtil
import com.charan.habitdiary.utils.PermissionManager
import com.charan.habitdiary.utils.ProcessState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyLogViewModel @Inject constructor(
    private val habitLocalRepository: HabitLocalRepository,
    private val fileRepository: FileRepository,
    private val permissionManager: PermissionManager,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DailyLogState())
    val state: StateFlow<DailyLogState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DailyLogEffect>()
    val effect: SharedFlow<DailyLogEffect> = _effect.asSharedFlow()

    init {
        observeDateTimeChanges()
        observeHourFormat()
    }

    fun onEvent(event: DailyLogEvent) {
        when (event) {
            is DailyLogEvent.InitializeLog -> initializeLog(event.habitId)
            is DailyLogEvent.OnNotesTextChange -> updateNotes(event.text)
            is DailyLogEvent.OnImagePathChange -> updateImagePath(event.path)
            DailyLogEvent.OnSaveDailyLogClick -> saveDailyLog()
            DailyLogEvent.OnBackClick -> sendEffect(DailyLogEffect.OnNavigateBack)
            is DailyLogEvent.OnToggleImagePickOptionsSheet -> toggleImagePickerSheet(event.isVisible)
            DailyLogEvent.OnPickFromGalleryClick -> sendEffect(DailyLogEffect.OnOpenImagePicker)
            DailyLogEvent.OnTakePhotoClick -> handleTakePhoto()
            is DailyLogEvent.OnImagePick -> handleImagePicked(event.uri)
            DailyLogEvent.OnOpenSettingsForPermissions -> permissionManager.openSettingsPermissionScreen()
            is DailyLogEvent.ToggleShowRationaleForCameraPermission -> setCameraRationaleVisible(event.showRationale)
            is DailyLogEvent.OnToggleDateSelectorDialog -> setDatePickerVisible(event.isVisible)
            is DailyLogEvent.OnDateSelected -> updateDate(event.timeMillis)
            is DailyLogEvent.OnToggleTimeSelectorDialog -> setTimePickerVisible(event.isVisible)
            is DailyLogEvent.OnTimeSelected -> updateTime(event.timeMillis)
            DailyLogEvent.OnDeleteDailyLog -> deleteDailyLog()
            is DailyLogEvent.OnToggleDeleteDialog -> setDeleteDialogVisible(event.showDeleteDialog)
        }
    }

    private fun setDeleteDialogVisible(visible: Boolean) {
        _state.update { it.copy(showDeleteDialog = visible) }
    }

    private fun initializeLog(logId: Int?) = viewModelScope.launch(Dispatchers.IO) {
        if (logId != null) {
            val log = habitLocalRepository.getDailyLogsWithHabitWithId(logId)
            _state.update {
                it.copy(
                    dailyLogItemDetails = log.toDailyLogItemDetails(),
                    isEdit = true
                )
            }
        } else {
            val now = System.currentTimeMillis()
            _state.update {
                it.copy(
                    dailyLogItemDetails = it.dailyLogItemDetails.copy(
                        dateMillis = now,
                        timeMillis = now
                    )
                )
            }
        }
    }

    private fun updateNotes(text: String) {
        _state.update {
            it.copy(
                dailyLogItemDetails = it.dailyLogItemDetails.copy(
                    notesText = text
                )
            )
        }
    }

    private fun updateImagePath(path: String) {
        _state.update {
            it.copy(
                dailyLogItemDetails = it.dailyLogItemDetails.copy(imagePath = path),
                showImagePickOptionsSheet = false
            )
        }
    }

    private fun toggleImagePickerSheet(visible: Boolean) {
        _state.update { it.copy(showImagePickOptionsSheet = visible) }
    }

    private fun setCameraRationaleVisible(visible: Boolean) {
        _state.update { it.copy(showRationaleForCameraPermission = visible) }
    }

    private fun setDatePickerVisible(visible: Boolean) {
        _state.update { it.copy(showDateSelectDialog = visible) }
    }

    private fun setTimePickerVisible(visible: Boolean) {
        _state.update { it.copy(showTimeSelectDialog = visible) }
    }

    private fun updateDate(dateMillis: Long) {
        _state.update {
            it.copy(
                dailyLogItemDetails = it.dailyLogItemDetails.copy(dateMillis = dateMillis),
                showDateSelectDialog = false
            )
        }
    }

    private fun updateTime(timeMillis: Long) {
        _state.update {
            it.copy(
                dailyLogItemDetails = it.dailyLogItemDetails.copy(timeMillis = timeMillis),
                showTimeSelectDialog = false
            )
        }
    }

    private fun saveDailyLog() = viewModelScope.launch(Dispatchers.IO) {
        habitLocalRepository.upsetDailyLog(
            _state.value.toDailyLogEntity()
        )
        sendEffect(DailyLogEffect.OnNavigateBack)
    }

    private fun handleTakePhoto() {
        if (!permissionManager.isCameraPermissionGranted()) {
            sendEffect(DailyLogEffect.OnRequestCameraPermission)
            return
        }
        val uri = fileRepository.createImageUri()
        _state.update {
            it.copy(
                tempImagePath = uri.toString()
            )
        }
        sendEffect(DailyLogEffect.OnTakePhoto)
    }

    private fun handleImagePicked(uri: Uri) = viewModelScope.launch {
        fileRepository.saveImage(uri).collectLatest { result ->
            if (result is ProcessState.Success<*>) {
                updateImagePath(result.data as String)
            }
        }
    }

    private fun deleteDailyLog() = viewModelScope.launch(Dispatchers.IO) {
        val id = _state.value.dailyLogItemDetails.id ?: return@launch
        habitLocalRepository.deleteDailyLog(id)
        sendEffect(DailyLogEffect.OnNavigateBack)
    }

    private fun observeDateTimeChanges() = viewModelScope.launch {
        _state
            .map { it.dailyLogItemDetails.dateMillis to it.dailyLogItemDetails.timeMillis }
            .distinctUntilChanged()
            .collectLatest { (dateMillis, timeMillis) ->
                _state.update { current ->
                    current.copy(
                        dailyLogItemDetails = current.dailyLogItemDetails.copy(
                            formattedDateString = DateUtil.getDateStringFromMillis(dateMillis),
                            formattedTimeString = DateUtil.convertTimeMillisToTimeString(
                                timeMillis,
                                dataStoreRepository.getIs24HourFormat.first()
                            )
                        )
                    )
                }
            }
    }

    private fun observeHourFormat() = viewModelScope.launch {
        dataStoreRepository.getIs24HourFormat.collectLatest { format24 ->
            _state.update { it.copy(is24HourFormat = format24) }
        }
    }

    private fun sendEffect(effect: DailyLogEffect) = viewModelScope.launch {
        _effect.emit(effect)
    }
}
