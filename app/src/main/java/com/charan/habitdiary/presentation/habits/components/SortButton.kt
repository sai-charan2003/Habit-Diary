package com.charan.habitdiary.presentation.habits.components

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.charan.habitdiary.data.model.enums.HabitSortType
import com.charan.habitdiary.presentation.common.components.CustomDropDown

/**
 * Renders a text button that shows the currently selected sort label and anchors a dropdown to choose a different habit sort.
 *
 * @param modifier Modifier applied to the TextButton.
 * @param onClick Callback invoked when the button is clicked (typically toggles the dropdown).
 * @param onSortSelected Called with the selected HabitSortType when the user picks an item from the dropdown.
 * @param selectedSortTypeRes Resource ID of the currently selected sort label (string resource).
 * @param isExpanded Controls whether the dropdown is visible.
 */
@Composable
fun SortButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onSortSelected: (HabitSortType) -> Unit,
    selectedSortTypeRes: Int,
    isExpanded: Boolean
) {

    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(stringResource(selectedSortTypeRes))

        CustomDropDown(
            items = HabitSortType.entries.map { it.toLocaleString() },
            selectedItem = selectedSortTypeRes,
            onItemSelected = {
                onSortSelected(HabitSortType.fromRes(it as Int))
            },
            isExpanded = isExpanded,
            onDismiss = onClick
        )
    }
}