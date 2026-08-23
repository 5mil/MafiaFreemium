package com.mafiagame.freemium.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mafiagame.freemium.ui.navigation.AppRoute
import com.mafiagame.freemium.ui.theme.MafiaTheme
import kotlinx.coroutines.launch

/**
 * Top-level composable that owns navigation and wires the ViewModel
 * to every screen.
 *
 * Usage from MainActivity:
 *
 *   setContent {
 *       MafiaApp(viewModel = viewModel())
 *   }
 */
@Composable
fun MafiaApp(
    viewModel: MafiaViewModel
) {
    MafiaTheme {
        val route by viewModel.currentRoute.collectAsStateWithLifecycle()
        val wallet by viewModel.wallet.collectAsStateWithLifecycle()
        val engine by viewModel.gameEngine.collectAsStateWithLifecycle()
        val revealIndex by viewModel.revealIndex.collectAsStateWithLifecycle()
        val toast by viewModel.toastMessage.collectAsStateWithLifecycle()

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        LaunchedEffect(toast) {
            toast?.let {
                scope.launch {
                    snackbarHostState.showSnackbar(it)
                    viewModel.clearToast()
                }
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                when (route) {
                    AppRoute.Home -> HomeScreen(
                        wallet = wallet,
                        onStartGame = { viewModel.navigateTo(AppRoute.Setup) },
                        onOpenShop = { viewModel.navigateTo(AppRoute.Shop) },
                        onOpenSettings = { viewModel.navigateTo(AppRoute.Settings) }
                    )

                    AppRoute.Shop -> ShopScreen(
                        wallet = wallet,
                        onBuyProduct = { productId -> viewModel.onBuyProduct(productId) },
                        onBack = { viewModel.navigateTo(AppRoute.Home) }
                    )

                    AppRoute.Setup -> GameSetupScreen(
                        onStartJob = { names -> viewModel.startJob(names) },
                        onBack = { viewModel.navigateTo(AppRoute.Home) }
                    )

                    AppRoute.RoleReveal -> {
                        val players = viewModel.getPlayers()
                        RoleRevealScreen(
                            players = players,
                            currentIndex = revealIndex,
                            onNext = { viewModel.nextReveal() },
                            onFinished = { viewModel.navigateTo(AppRoute.Night) }
                        )
                    }

                    AppRoute.Night -> {
                        val currentEngine = engine
                        if (currentEngine != null) {
                            NightPhaseScreen(
                                engine = currentEngine,
                                onFinishNight = { viewModel.finishNight() }
                            )
                        } else {
                            viewModel.goHome()
                        }
                    }

                    AppRoute.Day -> {
                        val currentEngine = engine
                        if (currentEngine != null) {
                            DayPhaseScreen(
                                engine = currentEngine,
                                onFinishDay = { viewModel.finishDay() }
                            )
                        } else {
                            viewModel.goHome()
                        }
                    }

                    AppRoute.Settings -> {
                        HomeScreen(
                            wallet = wallet,
                            onStartGame = { viewModel.navigateTo(AppRoute.Setup) },
                            onOpenShop = { viewModel.navigateTo(AppRoute.Shop) },
                            onOpenSettings = { viewModel.goHome() }
                        )
                    }
                }
            }
        }
    }
}