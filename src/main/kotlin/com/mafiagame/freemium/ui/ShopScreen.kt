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
import com.mafiagame.freemium.model.PlayerWallet
import com.mafiagame.freemium.model.ShopCatalog
import com.mafiagame.freemium.model.ShopProduct
import com.mafiagame.freemium.model.ProductType
import com.mafiagame.freemium.ui.theme.*

/**
 * Black Market (Shop) screen.
 * Pure microtransactions — Diamonds, Hits, roles, Made Man status.
 */
@Composable
fun ShopScreen(
    wallet: PlayerWallet,
    onBuyProduct: (String) -> Unit,   // productId
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkAlley)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SmokyGray)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onBack) {
                Text("← Back", color = SoftWhite)
            }
            Text("BLACK MARKET", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("${wallet.gems} ◆", color = DiamondBlue, fontWeight = FontWeight.Bold)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { SectionHeader("DIAMONDS") }
            items(ShopCatalog.products.filter { it.type == ProductType.GEM_PACK }) { product ->
                ProductCard(product = product, onBuy = { onBuyProduct(product.id) })
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeader("HITS — SKIP THE WAIT")
            }
            items(ShopCatalog.products.filter { it.type == ProductType.ENERGY_PACK }) { product ->
                ProductCard(product = product, onBuy = { onBuyProduct(product.id) })
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeader("RECRUIT SPECIALISTS")
            }
            items(ShopCatalog.products.filter { it.type == ProductType.ROLE_UNLOCK || it.type == ProductType.ROLE_PACK }) { product ->
                ProductCard(product = product, onBuy = { onBuyProduct(product.id) })
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeader("STATUS & POWER")
            }
            items(ShopCatalog.products.filter {
                it.type == ProductType.VIP_SUBSCRIPTION ||
                it.type == ProductType.BATTLE_PASS ||
                it.type == ProductType.STARTER_PACK
            }) { product ->
                ProductCard(product = product, onBuy = { onBuyProduct(product.id) })
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = GoldAccent,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun ProductCard(
    product: ShopProduct,
    onBuy: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SmokyGray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.title,
                    color = SoftWhite,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = product.description,
                    color = SoftWhite.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = onBuy,
                colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(product.priceUsd, fontSize = 13.sp)
            }
        }
    }
}