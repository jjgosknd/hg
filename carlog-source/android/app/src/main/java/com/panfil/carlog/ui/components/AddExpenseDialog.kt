package com.panfil.carlog.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.panfil.carlog.domain.Expense
import com.panfil.carlog.domain.ExpenseType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onSave: (Expense) -> Unit,
    /** Текущий пробег машины. Будет подставлен в поле как подсказка. */
    currentMileage: Int = 0,
) {
    var selectedType by remember { mutableStateOf(ExpenseType.FUEL) }
    var amount by remember { mutableStateOf("") }
    var mileage by remember {
        mutableStateOf(if (currentMileage > 0) currentMileage.toString() else "")
    }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Принимаем и точку, и запятую — Russian-friendly.
    val parsedAmount = amount.replace(',', '.').toDoubleOrNull() ?: 0.0
    val isValid = parsedAmount > 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_expense)) },
        text = {
            Column {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                ) {
                    OutlinedTextField(
                        value = selectedType.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.expense_type)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        ExpenseType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.label) },
                                onClick = {
                                    selectedType = type
                                    expanded = false
                                },
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = amount,
                    // Разрешаем цифры, точку И запятую.
                    onValueChange = { input ->
                        amount = input.filter { c -> c.isDigit() || c == '.' || c == ',' }
                    },
                    label = { Text(stringResource(R.string.amount_rub)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    isError = amount.isNotEmpty() && !isValid,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = mileage,
                    onValueChange = { mileage = it.filter { c -> c.isDigit() } },
                    label = { Text(stringResource(R.string.mileage_km)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.description_optional)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = isValid,
                onClick = {
                    onSave(
                        Expense(
                            type = selectedType,
                            amount = parsedAmount,
                            mileage = mileage.toIntOrNull() ?: 0,
                            description = description.trim(),
                        ),
                    )
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
