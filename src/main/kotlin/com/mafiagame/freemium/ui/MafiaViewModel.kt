package com.mafiagame.freemium.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mafiagame.freemium.billing.BillingManager
import com.mafiagame.freemium.billing.MockBillingManager
import com.mafiagame.freemium.data.EconomyManager
import com.mafiagame.freemium.model.GameEngine
import com.mafiagame.freemium.model.Player
import com.mafiagame.freemium.model.PlayerWallet
import com.mafiagame.freemium.ui.navigation.AppRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Central ViewModel that owns:
 * - Economy / wallet state
 * - Current game session
 * - Navigation target
 * - Billing results (mock or real)
 */
class MafiaViewModel(
    private val economyManager: EconomyManager = EconomyManager(),
    private val useMockBilling: Boolean = true
) : ViewModel() {

    private val _currentRoute = MutableStateFlow<AppRoute>(AppRoute.Home)
    val currentRoute: StateFlow<AppRoute> = _currentRoute.asStateFlow()

    private val _wallet = MutableStateFlow(economyManager.getWallet())
    val wallet: StateFlow<PlayerWallet> = _wallet.asStateFlow()

    private val _gameEngine = MutableStateFlow<GameEngine?>(null)
    val gameEngine: StateFlow<GameEngine?> = _gameEngine.asStateFlow()

    private val _revealIndex = MutableStateFlow(0)
    val revealIndex: StateFlow<Int> = _revealIndex.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val mockBilling = MockBillingManager(economyManager)

    fun navigateTo(route: AppRoute) {
        _currentRoute.value = route
    }

    fun goHome() {
        _currentRoute.value = AppRoute.Home
        _gameEngine.value = null
        _revealIndex.value = 0
    }

    fun refreshWallet() {
        _wallet.value = economyManager.getWallet().copy(
            unlockedRoles = economyManager.getWallet().unlockedRoles.toMutableSet()
        )
    }

    fun onBuyProduct(productId: String) {
        viewModelScope.launch {
            val realMoneyIds = setOf(
                "gems_100", "gems_500", "gems_1200", "gems_2500", "gems_6500", "gems_14000",
                "energy_unlimited_24h", "roles_all_premium", "starter_pack",
                "vip_monthly", "vip_yearly", "battle_pass"
            )

            if (useMockBilling) {
                if (productId in realMoneyIds) {
                    mockBilling.simulatePurchase(productId)
                } else {
                    mockBilling.simulateBuyWithDiamonds(productId)
                }

                mockBilling.purchaseResult.value?.let { result ->
                    when (result) {
                        is BillingManager.PurchaseResult.Success -> {
                            refreshWallet()
                            _toastMessage.value = "Purchase successful: $productId"
                        }
                        is BillingManager.PurchaseResult.Error -> {
                            _toastMessage.value = result.message
                        }
                        else -> {}
                    }
                    mockBilling.clearResult()
                }
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun startJob(playerNames: List<String>) {
        if (playerNames.size !in 6..20) {
            _toastMessage.value = "Need 6–20 players"
            return
        }

        if (!economyManager.canStartGame()) {
            _toastMessage.value = "No Hits left. Buy more in the Black Market."
            navigateTo(AppRoute.Shop)
            return
        }

        if (!economyManager.consumeEnergyForGame()) {
            _toastMessage.value = "Could not start job — no Hits"
            return
        }
        refreshWallet()

        val unlocked = economyManager.getWallet().unlockedRoles
        val engine = GameEngine(playerNames, unlocked)
        engine.assignRoles()

        _gameEngine.value = engine
        _revealIndex.value = 0
        navigateTo(AppRoute.RoleReveal)
    }

    fun nextReveal() {
        val engine = _gameEngine.value ?: return
        val next = _revealIndex.value + 1
        if (next >= engine.players.size) {
            navigateTo(AppRoute.Night)
        } else {
            _revealIndex.value = next
        }
    }

    fun finishNight() {
        val engine = _gameEngine.value ?: return
        engine.processNight(
            mafiaTargetId = null,
            doctorTargetId = null,
            detectiveTargetId = null
        )
        if (engine.winner != null) {
            _toastMessage.value = "Game over — ${engine.winner} wins!"
            goHome()
        } else {
            navigateTo(AppRoute.Day)
        }
    }

    fun finishDay() {
        val engine = _gameEngine.value ?: return
        engine.processDayVote(emptyMap())
        if (engine.winner != null) {
            _toastMessage.value = "Game over — ${engine.winner} wins!"
            economyManager.addCoins(25)
            refreshWallet()
            goHome()
        } else {
            navigateTo(AppRoute.Night)
        }
    }

    fun getPlayers(): List<Player> = _gameEngine.value?.players ?: emptyList()
}