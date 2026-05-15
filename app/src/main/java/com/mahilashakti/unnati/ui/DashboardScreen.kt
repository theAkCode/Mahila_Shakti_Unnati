package com.mahilashakti.unnati.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahilashakti.unnati.viewmodel.LoanViewModel
import com.mahilashakti.unnati.viewmodel.SavingsViewModel
import java.text.SimpleDateFormat
import java.util.*

// ── Color tokens ──────────────────────────────────────────────────────────────
private val Navy      = Color(0xFF1B3A6B)
private val NavyLight = Color(0xFF2E5FA3)
private val NavyMid   = Color(0xFF4A7BC4)
private val Teal      = Color(0xFF0D9488)
private val TealLight = Color(0xFF14B8A6)
private val Amber     = Color(0xFFF59E0B)
private val Rose      = Color(0xFFE11D48)
private val Purple    = Color(0xFF7C3AED)
private val GrayBg    = Color(0xFFF1F5F9)
private val CardWhite = Color(0xFFFFFFFF)
private val TextPrim  = Color(0xFF0F172A)
private val TextSec   = Color(0xFF64748B)
private val TextHint  = Color(0xFF94A3B8)

@Composable
fun DashboardScreen(
    savingsVM: SavingsViewModel,
    loanVM: LoanViewModel,
    onMembersClick: () -> Unit
) {
    val totalSavings by savingsVM.totalSavings.observeAsState(0.0)
    val totalIssued  by loanVM.totalIssued.observeAsState(0.0)
    val totalOwed    by loanVM.totalOutstanding.observeAsState(0.0)
    val groupHealth  = if (totalIssued == 0.0) 100f
    else ((1.0 - totalOwed / totalIssued) * 100)
        .toFloat().coerceIn(0f, 100f)

    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 5..11  -> "Good morning"
        in 12..16 -> "Good afternoon"
        else      -> "Good evening"
    }
    val today = SimpleDateFormat("EEEE, d MMM", Locale.getDefault()).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBg)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        HeaderSection(greeting, today)

        // Hero card — overlaps header bottom
        HeroFundCard(totalSavings, totalOwed, groupHealth)

        Spacer(Modifier.height(8.dp))

        // 3 stat cards
        StatRow(totalIssued, totalOwed, totalSavings)

        Spacer(Modifier.height(24.dp))

        // Quick actions — only Members is wired
        SectionTitle("Quick Actions")
        Spacer(Modifier.height(12.dp))
        QuickActionsRow(onMembersClick)

        Spacer(Modifier.height(24.dp))

        // Financial health gauge — computed from real data
        SectionTitle("Group Financial Health")
        Spacer(Modifier.height(12.dp))
        HealthCard(groupHealth, totalSavings, totalOwed)

        Spacer(Modifier.height(32.dp))
    }
}

// ── Header ────────────────────────────────────────────────────────────────────
@Composable
private fun HeaderSection(greeting: String, today: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Navy, NavyLight)))
            .padding(horizontal = 20.dp)
            .padding(top = 52.dp, bottom = 28.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = today,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.65f),
                    letterSpacing = 0.5.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "$greeting 👋",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Mahila Shakti Unnati",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.75f)
                )
            }
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
                    .border(1.5.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Group,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// ── Hero fund card ────────────────────────────────────────────────────────────
@Composable
private fun HeroFundCard(totalSavings: Double, totalOwed: Double, health: Float) {
    val animatedSavings by animateFloatAsState(
        targetValue = totalSavings.toFloat(),
        animationSpec = tween(1000, easing = EaseOutCubic),
        label = "savings"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (-20).dp)
            .shadow(12.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(NavyMid, Teal)))
            .padding(24.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.AccountBalance, null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    "Total Group Fund",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(Modifier.weight(1f))
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.15f)
                ) {
                    Text(
                        "  Active  ",
                        fontSize = 11.sp,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "₹ %,.2f".format(animatedSavings),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
            Spacer(Modifier.height(14.dp))

            Row(Modifier.fillMaxWidth()) {
                MiniStat(
                    "Outstanding", "₹ %,.0f".format(totalOwed),
                    Icons.Default.TrendingDown, Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(36.dp)
                        .background(Color.White.copy(alpha = 0.2f))
                        .align(Alignment.CenterVertically)
                )
                MiniStat(
                    "Fund Health", "%.0f%%".format(health),
                    Icons.Default.FavoriteBorder, Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MiniStat(
    label: String, value: String,
    icon: ImageVector, modifier: Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null,
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Column {
            Text(label, fontSize = 11.sp, color = Color.White.copy(alpha = 0.65f))
            Text(value, fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}

// ── Stat row ──────────────────────────────────────────────────────────────────
@Composable
private fun StatRow(totalIssued: Double, totalOwed: Double, totalSavings: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MiniStatCard(
            "Loans Issued", "₹ %,.0f".format(totalIssued),
            Icons.Default.CreditCard, Amber, Modifier.weight(1f)
        )
        MiniStatCard(
            "Outstanding", "₹ %,.0f".format(totalOwed),
            Icons.Default.Warning, Rose, Modifier.weight(1f)
        )
        MiniStatCard(
            "Total Savings", "₹ %,.0f".format(totalSavings),
            Icons.Default.Savings, Purple, Modifier.weight(1f)
        )
    }
}

@Composable
private fun MiniStatCard(
    label: String, value: String,
    icon: ImageVector, accent: Color,
    modifier: Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accent.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = accent, modifier = Modifier.size(16.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(
                value, fontSize = 13.sp, fontWeight = FontWeight.Bold,
                color = TextPrim, maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            Text(label, fontSize = 10.sp, color = TextHint)
        }
    }
}

// ── Quick actions ─────────────────────────────────────────────────────────────
@Composable
private fun QuickActionsRow(onMembersClick: () -> Unit) {
    // Only Members is navigable — Savings and Loans
    // are accessible via Members → member tap
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionButton(
            label = "Members",
            icon = Icons.Default.Group,
            color = NavyLight,
            modifier = Modifier.weight(1f),
            onClick = onMembersClick
        )
        QuickActionButton(
            label = "Savings",
            icon = Icons.Default.Savings,
            color = Teal,
            modifier = Modifier.weight(1f),
            subtitle = "Via Members",
            onClick = onMembersClick
        )
        QuickActionButton(
            label = "Loans",
            icon = Icons.Default.AccountBalance,
            color = Amber,
            modifier = Modifier.weight(1f),
            subtitle = "Via Members",
            onClick = onMembersClick
        )
    }
}

@Composable
private fun QuickActionButton(
    label: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier,
    subtitle: String = "",
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(CardWhite)
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextPrim)
        if (subtitle.isNotEmpty()) {
            Text(subtitle, fontSize = 9.sp, color = TextHint)
        }
    }
}

// ── Health card ───────────────────────────────────────────────────────────────
@Composable
private fun HealthCard(health: Float, savings: Double, owed: Double) {
    val animatedHealth by animateFloatAsState(
        targetValue = health / 100f,
        animationSpec = tween(1200, easing = EaseOutCubic),
        label = "health"
    )
    val healthColor = if (health > 70) Teal else if (health > 40) Amber else Rose
    val healthLabel = if (health > 70) "Healthy" else if (health > 40) "Moderate" else "Needs Attention"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Repayment Rate", fontSize = 13.sp, color = TextSec)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "%.1f%%".format(health),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = healthColor
                    )
                    Spacer(Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = healthColor.copy(alpha = 0.1f)
                    ) {
                        Text(
                            "  $healthLabel  ",
                            fontSize = 11.sp,
                            color = healthColor,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
                // Circular gauge
                Box(
                    modifier = Modifier.size(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.fillMaxSize(),
                        color = GrayBg,
                        strokeWidth = 8.dp
                    )
                    CircularProgressIndicator(
                        progress = { animatedHealth },
                        modifier = Modifier.fillMaxSize(),
                        color = healthColor,
                        strokeWidth = 8.dp
                    )
                    Icon(
                        Icons.Default.Favorite, null,
                        tint = healthColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { animatedHealth },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = healthColor,
                trackColor = GrayBg
            )
            Spacer(Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth()) {
                Text("0%", fontSize = 10.sp, color = TextHint)
                Spacer(Modifier.weight(1f))
                Text("100%", fontSize = 10.sp, color = TextHint)
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFE2E8F0))
            Spacer(Modifier.height(14.dp))

            Row(Modifier.fillMaxWidth()) {
                LegendItem("Total Savings", "₹ %,.0f".format(savings),
                    Teal, Modifier.weight(1f))
                LegendItem("Outstanding", "₹ %,.0f".format(owed),
                    Rose, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun LegendItem(label: String, value: String, dot: Color, modifier: Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(dot)
        )
        Spacer(Modifier.width(6.dp))
        Column {
            Text(label, fontSize = 11.sp, color = TextHint)
            Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrim)
        }
    }
}

// ── Section title ─────────────────────────────────────────────────────────────
@Composable
private fun SectionTitle(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(18.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(NavyLight)
        )
        Spacer(Modifier.width(8.dp))
        Text(title, fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold, color = TextPrim)
    }
}