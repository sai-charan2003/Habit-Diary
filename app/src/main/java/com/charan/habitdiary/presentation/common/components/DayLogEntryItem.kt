import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DayLogEntryItem(
    time: String,
    note: String,
    imagePath: String = "",
    habitName: String = "",
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.labelLargeEmphasized.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            modifier = Modifier.width(65.dp).padding(top = 5.dp)
        )

        LogEntryCard(
            note = note,
            imagePath = imagePath,
            habitName = habitName,
            onClick = onClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LogEntryCard(
    note: String,
    imagePath: String = "",
    habitName: String = "",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .then(
                if (!(habitName.isEmpty())) {
                    Modifier.border(
                        width = 0.5.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        shape = CardDefaults.shape
                    )
                } else {
                    Modifier
                }
            )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if(habitName.isNotEmpty()){
                Text(
                    text = "Habit Completed",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = habitName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
            if (imagePath.isNotEmpty()) {
                AsyncImage(
                    model = imagePath,
                    contentDescription = "Log Entry Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                    contentScale = ContentScale.Fit
                )
            }
            if (note.isNotEmpty()) {
                Text(
                    text = note,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.4f
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DayLogEntryItemPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DayLogEntryItem(
                time = "09:30 AM",
                note = "Completed 30 minutes of running followed by 15 push-ups. Feeling energized and ready to tackle the day!",
                imagePath = "https://picsum.photos/400/200",

                habitName = "Morning Workout"
            )

            DayLogEntryItem(
                time = "02:15 PM",
                note = "Quick meditation session during lunch break. Really helped clear my mind.",

                habitName = "Mindfulness"
            )

            DayLogEntryItem(
                time = "08:45 PM",
                note = "",

                habitName = "Evening Stretch"
            )

            DayLogEntryItem(
                time = "11:20 AM",
                note = "Had a great conversation with the team about the new project direction.",

            )
        }
    }
}