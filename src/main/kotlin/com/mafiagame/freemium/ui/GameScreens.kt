package com.mafiagame.freemium.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mafiagame.freemium.model.GameEngine
import com.mafiagame.freemium.model.Player
import com.mafiagame.freemium.ui.theme.*

/**
 * Simple role reveal screen (pass the phone around).
 */
@Composable
fun RoleRevealScreen(
    players: List<Player>,
    currentIndex: Int,
    onNext: () -> Unit,
    onFinished: () -> Unit
) {
    val player = players.getOrNull(currentIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkAlley)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (player == null) {
            Text("All roles assigned", color = SoftWhite)
            Button(onClick = onFinished, colors = ButtonDefaults.buttonColors(containerColor = BloodRed)) {
                Text("Begin Night")
            }
            return
        }

        Text("Pass the phone to", color = SoftWhite.copy(alpha = 0.7f))
        Text(
            text = player.name,
            color = GoldAccent,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = SmokyGray),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Your Role", color = SoftWhite.copy(alpha = 0.6f))
                Text(
                    text = player.role?.name ?: "???",
                    color = BloodRed,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = player.role?.description ?: "",
                    color = SoftWhite.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (currentIndex < players.lastIndex) "NEXT PLAYER" else "START NIGHT",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Night phase placeholder — narrator controls.
 */
@Composable
fun NightPhaseScreen(
    engine: GameEngine,
    onFinishNight: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NightBlack)
            .padding(24.dp)
    ) {
        Text(
            text = "NIGHT ${engine.dayNumber}",
            color = BloodRed,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "The city sleeps… the family moves.",
            color = SoftWhite.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Alive players", color = GoldAccent, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(engine.getAlivePlayers()) { player ->
                Text("• ${player.name}", color = SoftWhite)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onFinishNight,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("RESOLVE NIGHT → DAY", fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * Day phase / voting placeholder.
 */
@Composable
fun DayPhaseScreen(
    engine: GameEngine,
    onFinishDay: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkAlley)
            .padding(24.dp)
    ) {
        Text(
            text = "DAY ${engine.dayNumber}",
            color = GoldAccent,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Discuss. Accuse. Vote.",
            color = SoftWhite.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Alive players", color = GoldAccent, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(engine.getAlivePlayers()) { player ->
                Text("• ${player.name}", color = SoftWhite)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onFinishDay,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("RESOLVE VOTE → NIGHT", fontWeight = FontWeight.Bold)
        }
    }
}