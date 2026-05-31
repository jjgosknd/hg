package com.panfil.carlog.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.panfil.carlog.R
import com.panfil.carlog.data.CarDatabase
import com.panfil.carlog.domain.CarInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCarDialog(
    initial: CarInfo,
    onDismiss: () -> Unit,
    onSave: (CarInfo) -> Unit,
) {
    var selectedBrand by remember { mutableStateOf(initial.brand) }
    var selectedModel by remember { mutableStateOf(initial.model) }
    var selectedGeneration by remember { mutableStateOf(initial.generation) }
    var selectedYearFrom by remember { mutableStateOf(initial.yearFrom) }
    var selectedYearTo by remember { mutableStateOf(initial.yearTo) }
    var mileage by remember { mutableStateOf(if (initial.mileage > 0) initial.mileage.toString() else "") }

    var brandExpanded by remember { mutableStateOf(false) }
    var modelExpanded by remember { mutableStateOf(false) }
    var generationExpanded by remember { mutableStateOf(false) }

    val brands = remember { CarDatabase.brandNames() }
    val models = remember(selectedBrand) { CarDatabase.modelsFor(selectedBrand) }
    val generations = remember(selectedBrand, selectedModel) {
        CarDatabase.generationsFor(selectedBrand, selectedModel)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.car_info_title)) },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = 420.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Brand logo + name
                if (selectedBrand.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(CarDatabase.logoFor(selectedBrand)),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            buildString {
                                append(selectedBrand)
                                if (selectedModel.isNotEmpty()) append(" $selectedModel")
                                if (selectedGeneration.isNotEmpty() && selectedYearFrom > 0) {
                                    append(" $selectedGeneration ($selectedYearFrom–$selectedYearTo)")
                                }
                            },
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                }

                // Brand selector
                ExposedDropdownMenuBox(
                    expanded = brandExpanded,
                    onExpandedChange = { brandExpanded = it },
                ) {
                    OutlinedTextField(
                        value = selectedBrand,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.brand)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(brandExpanded) },
                        leadingIcon = if (selectedBrand.isNotEmpty()) {
                            {
                                Image(
                                    painter = painterResource(CarDatabase.logoFor(selectedBrand)),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                )
                            }
                        } else null,
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                    )
                    ExposedDropdownMenu(
                        expanded = brandExpanded,
                        onDismissRequest = { brandExpanded = false },
                    ) {
                        brands.forEach { brand ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(CarDatabase.logoFor(brand)),
                                            contentDescription = brand,
                                            modifier = Modifier.size(28.dp),
                                        )
                                        Spacer(Modifier.width(12.dp))
                                        Text(brand)
                                    }
                                },
                                onClick = {
                                    selectedBrand = brand
                                    selectedModel = ""
                                    selectedGeneration = ""
                                    selectedYearFrom = 0
                                    selectedYearTo = 0
                                    brandExpanded = false
                                },
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Model selector
                ExposedDropdownMenuBox(
                    expanded = modelExpanded,
                    onExpandedChange = { if (selectedBrand.isNotEmpty()) modelExpanded = it },
                ) {
                    OutlinedTextField(
                        value = selectedModel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.model_label)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(modelExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        enabled = selectedBrand.isNotEmpty(),
                    )
                    ExposedDropdownMenu(
                        expanded = modelExpanded,
                        onDismissRequest = { modelExpanded = false },
                    ) {
                        models.forEach { model ->
                            DropdownMenuItem(
                                text = { Text(model.name) },
                                onClick = {
                                    selectedModel = model.name
                                    selectedGeneration = ""
                                    selectedYearFrom = 0
                                    selectedYearTo = 0
                                    modelExpanded = false
                                },
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Generation selector
                ExposedDropdownMenuBox(
                    expanded = generationExpanded,
                    onExpandedChange = { if (selectedModel.isNotEmpty()) generationExpanded = it },
                ) {
                    val genDisplay = if (selectedGeneration.isNotEmpty() && selectedYearFrom > 0) {
                        "$selectedGeneration ($selectedYearFrom–$selectedYearTo)"
                    } else {
                        selectedGeneration
                    }
                    OutlinedTextField(
                        value = genDisplay,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.generation_label)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(generationExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        enabled = selectedModel.isNotEmpty(),
                    )
                    ExposedDropdownMenu(
                        expanded = generationExpanded,
                        onDismissRequest = { generationExpanded = false },
                    ) {
                        generations.forEach { gen ->
                            DropdownMenuItem(
                                text = { Text("${gen.name} (${gen.yearFrom}–${gen.yearTo})") },
                                onClick = {
                                    selectedGeneration = gen.name
                                    selectedYearFrom = gen.yearFrom
                                    selectedYearTo = gen.yearTo
                                    generationExpanded = false
                                },
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Mileage input
                OutlinedTextField(
                    value = mileage,
                    onValueChange = { mileage = it.filter { c -> c.isDigit() } },
                    label = { Text(stringResource(R.string.mileage_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        CarInfo(
                            brand = selectedBrand,
                            model = selectedModel,
                            generation = selectedGeneration,
                            yearFrom = selectedYearFrom,
                            yearTo = selectedYearTo,
                            mileage = mileage.toIntOrNull() ?: 0,
                        ),
                    )
                },
                enabled = selectedBrand.isNotEmpty(),
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
