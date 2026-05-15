package com.mahilashakti.unnati.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahilashakti.unnati.data.Member
import com.mahilashakti.unnati.viewmodel.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembersScreen(
    vm: MemberViewModel,
    onMemberClick: (Member) -> Unit
) {
    val members by vm.members.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Members") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, "Add Member")
            }
        }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(members, key = { it.id }) { member ->
                MemberCard(member = member, onClick = { onMemberClick(member) })
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add New Member") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = name, onValueChange = { name = it; nameError = false },
                        label = { Text("Full Name") }, isError = nameError,
                        supportingText = { if (nameError) Text("Name is required") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phone, onValueChange = { phone = it; phoneError = false },
                        label = { Text("Phone Number") }, isError = phoneError,
                        supportingText = { if (phoneError) Text("Enter a valid 10-digit number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    nameError = name.isBlank()
                    phoneError = phone.length != 10 || phone.any { !it.isDigit() }
                    if (!nameError && !phoneError) {
                        vm.addMember(name, phone)
                        name = ""; phone = ""; showDialog = false
                    }
                }) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun MemberCard(member: Member, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Person, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(member.name, fontSize = 16.sp)
                Text(member.phone, fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
        }
    }
}