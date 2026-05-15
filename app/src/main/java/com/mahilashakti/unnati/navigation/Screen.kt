package com.mahilashakti.unnati.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Members   : Screen("members")
    object Savings   : Screen("savings/{memberId}") {
        fun createRoute(memberId: Int) = "savings/$memberId"
    }
    object Loan : Screen("loan/{memberId}") {
        fun createRoute(memberId: Int) = "loan/$memberId"
    }
}