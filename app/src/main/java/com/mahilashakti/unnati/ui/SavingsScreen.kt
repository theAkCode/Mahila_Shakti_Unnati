package com.mahilashakti.unnati.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahilashakti.unnati.data.SavingsEntry
import com.mahilashakti.unnati.viewmodel.SavingsViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsScreen(memberId: Int, memberName: String, vm: SavingsViewModel) {
    val entries by vm.savingsFor(memberId).observeAsState(emptyList())
    var amount by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Paid") }
    var amountError by remember { mutableStateOf(false) }

    Scaffold(topBar = { TopAppBar(title = { Text("Savings – $memberName") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Entry form
            Card(shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Record Savings", fontSize = 16.sp)
                    OutlinedTextField(
                        value = amount, onValueChange = { amount = it; amountError = false },
                        label = { Text("Amount (₹)") }, isError = amountError,
                        supportingText = { if (amountError) Text("Enter a valid amount") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Paid", "Pending").forEach { opt ->
                            FilterChip(selected = status == opt, onClick = { status = opt },
                                label = { Text(opt) })
                        }
                    }
                    Button(
                        onClick = {
                            val amt = amount.toDoubleOrNull()
                            amountError = (amt == null || amt <= 0)
                            if (!amountError) {
                                vm.recordSavings(memberId, amt!!, status)
                                amount = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Save Entry") }
                }
            }
            // History
            Text("Savings History", fontSize = 15.sp)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(entries) { SavingsEntryRow(it) }
            }
        }
    }
}

@Composable
fun SavingsEntryRow(entry: SavingsEntry) {
    val fmt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    Card(shape = RoundedCornerShape(8.dp)) {
        Row(Modifier.fillMaxWidth().padding(12.dp)) {
            Column(Modifier.weight(1f)) {
                Text(fmt.format(Date(entry.date)), fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                Text("₹ %.2f".format(entry.amount), fontSize = 16.sp)
            }
            AssistChip(
                onClick = {},
                label = { Text(entry.status, fontSize = 12.sp) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (entry.status == "Paid")
                        MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.errorContainer
                )
            )
        }
    }
}