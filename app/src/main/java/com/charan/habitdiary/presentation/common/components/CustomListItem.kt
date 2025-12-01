package com.charan.habitdiary.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.charan.habitdiary.ui.theme.IndexItem
import com.charan.habitdiary.ui.theme.roundedListItemCorners

@Composable
fun CustomListItem(
    indexItem: IndexItem,
    content : @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues = PaddingValues(10.dp),
    onClick : (() -> Unit)? = null
) {

    Surface(
        shape = roundedListItemCorners(indexItem),
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        color = MaterialTheme.colorScheme.surfaceContainer,
        enabled = onClick != null,
        onClick = { onClick?.invoke() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 60.dp)
                .padding(contentPaddingValues),
            content = content,
            verticalArrangement = Arrangement.Center
        )
    }

    Spacer(Modifier.height(2.dp))

}
