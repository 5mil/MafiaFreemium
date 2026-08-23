package com.mafiagame.freemium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mafiagame.freemium.data.EconomyManager
import com.mafiagame.freemium.data.WalletRepository
import com.mafiagame.freemium.ui.MafiaApp
import com.mafiagame.freemium.ui.MafiaViewModel

/**
 * Entry point for the Mafia Freemium Android app.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: MafiaViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = WalletRepository(applicationContext)
                val economy = EconomyManager(repository = repo)
                return MafiaViewModel(economyManager = economy, useMockBilling = true) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MafiaApp(viewModel = viewModel)
        }
    }
}