package com.mafiagame.freemium.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mafiagame.freemium.ui.theme.*

/**
 * Setup screen — enter player names before starting a job.
 */
@Composable
fun GameSetupScreen(
    onStartJob: (List<String>) -> Unit,
    onBack: () -> Unit
) {
    var playerNames by remember { mutableStateOf(listOf("", "", "", "", "", "")) }
    var newName by remember { mutableStateOf("") }

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
            text = "ASSEMBLE THE CREW",
            color = GoldAccent,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        Text(
            text = "Add 6–20 players. Minimum 6 to start a job.",
            color = SoftWhite.copy(alpha = 0.6f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("Player name") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldAccent,
                    unfocusedBorderColor = SoftWhite.copy(alpha = 0.3f),
                    focusedLabelColor = GoldAccent,
                    cursorColor = GoldAccent,
                    focusedTextColor = SoftWhite,
                    unfocusedTextColor = SoftWhite
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newName.isNotBlank() && playerNames.size < 20) {
                        playerNames = playerNames + newName.trim()
                        newName = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
                enabled = newName.isNotBlank() && playerNames.size < 20
            ) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${playerNames.count { it.isNotBlank() }} players",
            color = SoftWhite.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(playerNames.filter { it.isNotBlank() }) { index, name ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = SmokyGray),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${index + 1}. $name", color = SoftWhite)
                        TextButton(onClick = {
                            playerNames = playerNames.toMutableList().also { it.remove(name) }
                        }) {
                            Text("Remove", color = BloodRed, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val validPlayers = playerNames.filter { it.isNotBlank() }
        Button(
            onClick = { onStartJob(validPlayers) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
            shape = RoundedCornerShape(12.dp),
            enabled = validPlayers.size in 6..20
        ) {
            Text(
                text = if (validPlayers.size < 6) "Need at least 6 players" else "START THE JOB",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}