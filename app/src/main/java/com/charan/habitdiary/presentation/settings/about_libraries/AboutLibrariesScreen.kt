package com.charan.habitdiary.presentation.settings.about_libraries

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.charan.habitdiary.presentation.common.components.BackButton
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AboutLibrariesScreen(
    onBack : () -> Unit
) {
    Scaffold(
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text("About Libraries")
                },
                navigationIcon = {
                    BackButton {
                        onBack()
                    }
                }
            )
        }
    ) {innerPadding->
        LibrariesContainer(
            modifier = Modifier.padding(innerPadding)
        )

    }

}