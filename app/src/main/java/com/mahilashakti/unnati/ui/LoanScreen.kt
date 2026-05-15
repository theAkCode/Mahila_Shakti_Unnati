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
import com.mahilashakti.unnati.data.Loan
import com.mahilashakti.unnati.viewmodel.LoanViewModel
import com.mahilashakti.unnati.viewmodel.SavingsViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanScreen(
    memberId: Int, memberName: String,
    loanVM: LoanViewModel, savingsVM: SavingsViewModel
) {
    val loans by loanVM.loansFor(memberId).observeAsState(emptyList())
    val groupFund by savingsVM.totalSavings.observeAsState(0.0)
    val scope = rememberCoroutineScope()

    var loanAmount by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }
    var repayAmount by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var successMsg by remember { mutableStateOf<String?>(null) }

    Scaffold(topBar = { TopAppBar(title = { Text("Loans – $memberName") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {

            // Error / success snackbar-style
            errorMsg?.let {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(it, Modifier.padding(12.dp), color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }
            successMsg?.let {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Text(it, Modifier.padding(12.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }

            // Issue Loan
            Card(shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Issue Loan  •  Group fund: ₹ %.2f".format(groupFund), fontSize = 14.sp)
                    OutlinedTextField(value = loanAmount, onValueChange = { loanAmount = it },
                        label = { Text("Loan Amount (₹)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = interestRate, onValueChange = { interestRate = it },
                        label = { Text("Annual Interest Rate (%)") }, modifier = Modifier.fillMaxWidth())
                    Button(onClick = {
                        errorMsg = null; successMsg = null
                        scope.launch {
                            val err = loanVM.issueLoan(
                                memberId,
                                loanAmount.toDoubleOrNull() ?: -1.0,
                                interestRate.toDoubleOrNull() ?: -1.0,
                                groupFund
                            )
                            if (err == null) {
                                successMsg = "Loan issued successfully!"
                                loanAmount = ""; interestRate = ""
                            } else errorMsg = err
                        }
                    }, Modifier.fillMaxWidth()) { Text("Issue Loan") }
                }
            }

            // Record Repayment
            Card(shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Record Repayment", fontSize = 14.sp)
                    OutlinedTextField(value = repayAmount, onValueChange = { repayAmount = it },
                        label = { Text("Repayment Amount (₹)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedButton(onClick = {
                        errorMsg = null; successMsg = null
                        scope.launch {
                            val err = loanVM.recordRepayment(
                                memberId, repayAmount.toDoubleOrNull() ?: -1.0)
                            if (err == null) {
                                successMsg = "Repayment recorded!"
                                repayAmount = ""
                            } else errorMsg = err
                        }
                    }, Modifier.fillMaxWidth()) { Text("Submit Repayment") }
                }
            }

            Text("Loan History", fontSize = 15.sp)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(loans) { LoanRow(it) }
            }
        }
    }
}

@Composable
fun LoanRow(loan: Loan) {
    val fmt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    Card(shape = RoundedCornerShape(8.dp)) {
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    Text("Principal: ₹ %.2f".format(loan.principal), fontSize = 15.sp)
                    Text("Interest: ${loan.interestRate}% p.a.", fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    Text("Issued: ${fmt.format(Date(loan.issuedAt))}", fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                }
                Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                    Text("Balance", fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    Text("₹ %.2f".format(loan.balance), fontSize = 16.sp,
                        color = if (loan.status == "Repaid")
                            MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                    Text(loan.status, fontSize = 11.sp)
                }
            }
        }
    }
}