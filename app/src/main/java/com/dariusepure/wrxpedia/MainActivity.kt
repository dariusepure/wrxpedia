package com.dariusepure.wrxpedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.dariusepure.wrxpedia.data.repository.LocalWrxRepositoryImpl
import com.dariusepure.wrxpedia.ui.navigation.WrxNavGraph
import com.dariusepure.wrxpedia.ui.theme.WRXpediaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WRXpediaTheme {
                val repository = remember { LocalWrxRepositoryImpl(applicationContext) }
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WrxNavGraph(
                        navController = navController,
                        repository = repository
                    )
                }
            }
        }
    }
}
