package com.example.quiz.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownMenu(
    items: List<T>,
    selectedItem: T?,
    label: String,
    disabled: Boolean = false,
    onSelect: (item: T?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }

    if (selectedItem != null)
        search = selectedItem.toString()

    Column(modifier = Modifier.padding(8.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = if (disabled) false else it }
        ) {
            OutlinedTextField(
                value = search,
                onValueChange = {
                    search = it
                    expanded = true
                },
                label = { Text(text = label) },
                enabled = !disabled,
                trailingIcon = {
                    if (disabled) return@OutlinedTextField
                    if (selectedItem != null) {
                        IconButton(
                            onClick = {
                                search = "";
                                onSelect(null)
                                expanded = true
                            }) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "clear",
                            )
                        }
                    } else {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "arrow"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                val filteredItems = if (search.isNotEmpty()) {
                    items.filter { it.toString().contains(search, ignoreCase = true) }
                } else {
                    items
                }

                if (filteredItems.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text(text = "Brak wyników") },
                        onClick = {},
                        enabled = false
                    )
                } else {
                    filteredItems.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.toString()) },
                            onClick = {
                                expanded = false
                                search = it.toString()
                                onSelect(it)
                            },
                            enabled = !disabled
                        )
                    }
                }
            }
        }
    }
}