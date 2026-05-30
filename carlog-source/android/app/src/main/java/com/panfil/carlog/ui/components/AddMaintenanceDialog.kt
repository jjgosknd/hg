package com.panfil.carlog.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.panfil.carlog.R
import com.panfil.carlog.domain.Maintenance

@Composable
fun AddMaintenanceDialog(
    onDismiss: () -> Unit,
    onSave: (Maintenance) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var mileageInterval by remember { mutableStateOf("") }
    var monthsInterval by remember { mutableStateOf("") }
    var lastMileage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_maintenance)) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.maintenance_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = mileageInterval,
                    onValueChange = { mileageInterval = it.filter { c -> c.isDigit() } },
                    label = { Text(stringResource(R.string.mileage_interval_km)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = monthsInterval,
                    onValueChange = { monthsInterval = it.filter { c -> c.isDigit() } },
                    label = { Text(stringResource(R.string.months_interval)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = lastMileage,
                    onValueChange = { lastMileage = it.filter { c -> c.isDigit() } },
                    label = { Text(stringResource(R.string.last_service_mileage)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(
                            Maintenance(
                                title = title.trim(),
                                mileageInterval = mileageInterval.toIntOrNull() ?: 0,
                                monthsInterval = monthsInterval.toIntOrNull() ?: 0,
                                lastMileage = lastMileage.toIntOrNull() ?: 0,
                            ),
                        )
                    }
                },
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}
