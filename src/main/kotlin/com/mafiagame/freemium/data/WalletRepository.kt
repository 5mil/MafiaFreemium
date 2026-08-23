package com.mafiagame.freemium.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.mafiagame.freemium.model.PlayerWallet
import com.mafiagame.freemium.model.RoleId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Persists the player's wallet (Diamonds, Cash, Hits, Made Man status, unlocked roles)
 * using Jetpack DataStore so progress survives app restarts.
 *
 * Completely ad-free — only pure microtransaction state is stored.
 */

private val Context.walletDataStore: DataStore<Preferences> by preferencesDataStore(name = "mafia_wallet")

class WalletRepository(private val context: Context) {

    private object Keys {
        val COINS = intPreferencesKey("coins")
        val GEMS = intPreferencesKey("gems")
        val ENERGY = intPreferencesKey("energy")
        val MAX_ENERGY = intPreferencesKey("max_energy")
        val LAST_ENERGY_REFRESH = longPreferencesKey("last_energy_refresh")
        val IS_VIP = booleanPreferencesKey("is_vip")
        val UNLOCKED_ROLES = stringSetPreferencesKey("unlocked_roles")
    }

    val walletFlow: Flow<PlayerWallet> = context.walletDataStore.data.map { prefs ->
        prefs.toWallet()
    }

    suspend fun getWallet(): PlayerWallet {
        return context.walletDataStore.data.map { it.toWallet() }.first()
    }

    suspend fun saveWallet(wallet: PlayerWallet) {
        context.walletDataStore.edit { prefs ->
            prefs[Keys.COINS] = wallet.coins
            prefs[Keys.GEMS] = wallet.gems
            prefs[Keys.ENERGY] = wallet.energy
            prefs[Keys.MAX_ENERGY] = wallet.maxEnergy
            prefs[Keys.LAST_ENERGY_REFRESH] = wallet.lastEnergyRefresh
            prefs[Keys.IS_VIP] = wallet.isVip
            prefs[Keys.UNLOCKED_ROLES] = wallet.unlockedRoles.map { it.name }.toSet()
        }
    }

    suspend fun addGems(amount: Int) {
        context.walletDataStore.edit { prefs ->
            val current = prefs[Keys.GEMS] ?: 0
            prefs[Keys.GEMS] = current + amount
        }
    }

    suspend fun addCoins(amount: Int) {
        context.walletDataStore.edit { prefs ->
            val current = prefs[Keys.COINS] ?: 0
            prefs[Keys.COINS] = current + amount
        }
    }

    suspend fun setEnergy(value: Int) {
        context.walletDataStore.edit { prefs ->
            prefs[Keys.ENERGY] = value.coerceIn(0, 999)
        }
    }

    suspend fun unlockRole(roleId: RoleId) {
        context.walletDataStore.edit { prefs ->
            val current = prefs[Keys.UNLOCKED_ROLES] ?: defaultUnlockedRoles()
            prefs[Keys.UNLOCKED_ROLES] = current + roleId.name
        }
    }

    suspend fun setVip(enabled: Boolean) {
        context.walletDataStore.edit { prefs ->
            prefs[Keys.IS_VIP] = enabled
            if (enabled) {
                prefs[Keys.MAX_ENERGY] = 7
            }
        }
    }

    private fun Preferences.toWallet(): PlayerWallet {
        val roleNames = this[Keys.UNLOCKED_ROLES] ?: defaultUnlockedRoles()
        val roles = roleNames.mapNotNull { name ->
            try {
                RoleId.valueOf(name)
            } catch (_: Exception) {
                null
            }
        }.toMutableSet()

        roles.addAll(listOf(RoleId.VILLAGER, RoleId.MAFIA, RoleId.DOCTOR, RoleId.DETECTIVE))

        return PlayerWallet(
            coins = this[Keys.COINS] ?: 0,
            gems = this[Keys.GEMS] ?: 0,
            energy = this[Keys.ENERGY] ?: 5,
            maxEnergy = this[Keys.MAX_ENERGY] ?: 5,
            lastEnergyRefresh = this[Keys.LAST_ENERGY_REFRESH] ?: 0L,
            isVip = this[Keys.IS_VIP] ?: false,
            unlockedRoles = roles
        )
    }

    private fun defaultUnlockedRoles(): Set<String> = setOf(
        RoleId.VILLAGER.name,
        RoleId.MAFIA.name,
        RoleId.DOCTOR.name,
        RoleId.DETECTIVE.name
    )
}