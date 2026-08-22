package com.mafiagame.freemium.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mafiagame.freemium.model.PlayerWallet
import com.mafiagame.freemium.ui.theme.*

/**
 * Main home screen of the Mafia Freemium app.
 * Shows wallet status, start game, shop, and Made Man status.
 */
@Composable
fun HomeScreen(
    wallet: PlayerWallet,
    onStartGame: () -> Unit,
    onOpenShop: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkAlley)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = "MAFIA",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = BloodRed
        )
        Text(
            text = "FREEMIUM",
            fontSize = 18.sp,
            color = GoldAccent,
            letterSpacing = 4.sp
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Run the family. Rule the night.",
            color = SoftWhite.copy(alpha = 0.7f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Wallet card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SmokyGray),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Your Crew Status", color = GoldAccent, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WalletStat(label = "Diamonds", value = "${wallet.gems}", color = DiamondBlue)
                    WalletStat(label = "Cash", value = "${wallet.coins}", color = CashGreen)
                    WalletStat(label = "Hits", value = "${wallet.energy}", color = SoftWhite)
                }

                if (wallet.isVip) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "★ MADE MAN ★",
                        color = GoldAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Main actions
        Button(
            onClick = onStartGame,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("START A JOB", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onOpenShop,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldAccent),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("BLACK MARKET", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onOpenSettings) {
            Text("Settings", color = SoftWhite.copy(alpha = 0.6f))
        }
    }
}

@Composable
private fun WalletStat(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = color, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = SoftWhite.copy(alpha = 0.6f), fontSize = 12.sp)
    }
}