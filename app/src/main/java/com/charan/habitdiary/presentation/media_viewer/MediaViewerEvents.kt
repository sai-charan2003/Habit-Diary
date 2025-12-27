package com.charan.habitdiary.presentation.media_viewer

sealed class MediaViewerEvents {
    data class DownloadImage(val filePath : String) : MediaViewerEvents()

    data class ShareImage(val filePath : String) : MediaViewerEvents()
}