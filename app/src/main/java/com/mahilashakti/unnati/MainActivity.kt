package com.mahilashakti.unnati

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mahilashakti.unnati.data.AppDatabase
import com.mahilashakti.unnati.navigation.Screen
import com.mahilashakti.unnati.repository.AppRepository
import com.mahilashakti.unnati.ui.DashboardScreen
import com.mahilashakti.unnati.ui.LoanScreen
import com.mahilashakti.unnati.ui.MembersScreen
import com.mahilashakti.unnati.ui.SavingsScreen
import com.mahilashakti.unnati.ui.theme.MahilaShaktiTheme
import com.mahilashakti.unnati.viewmodel.LoanViewModel
import com.mahilashakti.unnati.viewmodel.MemberViewModel
import com.mahilashakti.unnati.viewmodel.SavingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getInstance(this)
        val repo = AppRepository(db.memberDao(), db.savingsDao(), db.loanDao())

        setContent {
            MahilaShaktiTheme { AppNavHost(repo) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(repo: AppRepository) {
    val navController = rememberNavController()

    val memberVM: MemberViewModel = viewModel(factory = MemberViewModel.Factory(repo))
    val savingsVM: SavingsViewModel = viewModel(factory = SavingsViewModel.Factory(repo))
    val loanVM: LoanViewModel = viewModel(factory = LoanViewModel.Factory(repo))

    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route

    val bottomItems = listOf(
        Triple(Screen.Dashboard, Icons.Default.Home, "Home"),
        Triple(Screen.Members, Icons.Default.Group, "Members"),
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomItems.forEach { (screen, icon, label) ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Dashboard.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    savingsVM = savingsVM,
                    loanVM = loanVM,
                    onMembersClick = { navController.navigate(Screen.Members.route) }
                )
            }

            composable(Screen.Members.route) {
                MembersScreen(vm = memberVM) { member ->
                    navController.navigate(Screen.Savings.createRoute(member.id))
                }
            }

            composable(
                route = Screen.Savings.route,
                arguments = listOf(navArgument("memberId") { type = NavType.IntType })
            ) { backStack ->
                val memberId = backStack.arguments!!.getInt("memberId")
                val members by memberVM.members.observeAsState(emptyList())
                val memberName = members.find { it.id == memberId }?.name ?: "Member"

                var showLoan by remember { mutableStateOf(false) }

                Column {
                    TabRow(selectedTabIndex = if (showLoan) 1 else 0) {
                        Tab(
                            selected = !showLoan,
                            onClick = { showLoan = false },
                            text = { Text("Savings") }
                        )
                        Tab(
                            selected = showLoan,
                            onClick = { showLoan = true },
                            text = { Text("Loans") }
                        )
                    }
                    if (showLoan) {
                        LoanScreen(
                            memberId = memberId,
                            memberName = memberName,
                            loanVM = loanVM,
                            savingsVM = savingsVM
                        )
                    } else {
                        SavingsScreen(
                            memberId = memberId,
                            memberName = memberName,
                            vm = savingsVM
                        )
                    }
                }
            }
        }
    }
}