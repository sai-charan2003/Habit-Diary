package com.charan.habitdiary.presentation.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.TonalToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.charan.habitdiary.data.model.enums.ThemeOption

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalLayoutApi::class)
@Composable
fun ThemeOptionButtonGroup(
    modifier: Modifier = Modifier,
    selectedTheme: ThemeOption,
    onSelectTheme: (ThemeOption) -> Unit
) {

    FlowRow(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ThemeOption.entries.forEach { option ->
            val isSelected = option == selectedTheme

            TonalToggleButton(
                checked = isSelected,
                onCheckedChange = { onSelectTheme(option) },
                modifier = Modifier
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Rounded.Done,
                        contentDescription = null,
                        modifier = Modifier
                            .size(ToggleButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))
                }
                Text(option.toString())
            }
        }
    }
}

@Preview
@Composable
fun ThemeOptionButtonGroupPreview() {
    var selectedTheme by remember {
        mutableStateOf(ThemeOption.SYSTEM_DEFAULT)
    }
    ThemeOptionButtonGroup(
        selectedTheme = selectedTheme,
        onSelectTheme = {
            selectedTheme = it

        }
    )
}