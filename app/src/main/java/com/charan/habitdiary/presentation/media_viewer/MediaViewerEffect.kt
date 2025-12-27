package com.charan.habitdiary.presentation.media_viewer

import android.net.Uri
import com.charan.habitdiary.presentation.common.model.ToastMessage

sealed class MediaViewerEffect {

    data class ShowToast(val message : ToastMessage) : MediaViewerEffect()

    data class ShareImage(val filePath : Uri) : MediaViewerEffect()
}