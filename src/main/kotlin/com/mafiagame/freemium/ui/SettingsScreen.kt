package com.mafiagame.freemium.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mafiagame.freemium.model.PlayerWallet
import com.mafiagame.freemium.ui.theme.*

@Composable
fun SettingsScreen(
    wallet: PlayerWallet,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkAlley)
            .padding(24.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("← Back", color = SoftWhite)
        }

        Text(
            text = "SETTINGS",
            color = GoldAccent,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = SmokyGray),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Crew Status", color = GoldAccent, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Diamonds: ${wallet.gems}", color = SoftWhite)
                Text("Cash: ${wallet.coins}", color = SoftWhite)
                Text("Hits: ${wallet.energy}", color = SoftWhite)
                Text(
                    text = if (wallet.isVip) "Status: MADE MAN ★" else "Status: Street crew",
                    color = if (wallet.isVip) GoldAccent else SoftWhite
                )
                Text("Unlocked roles: ${wallet.unlockedRoles.size}", color = SoftWhite)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = SmokyGray),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("About", color = GoldAccent, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Mafia Freemium — offline party narrator.\nPure microtransactions. No ads.\nRun the family. Rule the night.",
                    color = SoftWhite.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
        }
    }
}