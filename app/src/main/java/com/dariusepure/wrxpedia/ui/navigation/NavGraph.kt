package com.dariusepure.wrxpedia.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dariusepure.wrxpedia.data.repository.WrxRepository
import com.dariusepure.wrxpedia.ui.detail.DetailViewModel
import com.dariusepure.wrxpedia.ui.detail.GenerationDetailScreen
import com.dariusepure.wrxpedia.ui.home.HomeScreen
import com.dariusepure.wrxpedia.ui.home.HomeViewModel
import com.dariusepure.wrxpedia.ui.quicklookup.QuickLookupScreen
import com.dariusepure.wrxpedia.ui.quicklookup.QuickLookupType
import com.dariusepure.wrxpedia.ui.quicklookup.QuickLookupViewModel

object WrxDestinations {
    const val HOME = "home"
    const val DETAIL = "detail/{generationId}"
    const val QUICK_LOOKUP = "quick_lookup/{type}"

    fun detailRoute(generationId: String): String = "detail/$generationId"
    fun quickLookupRoute(type: QuickLookupType): String = "quick_lookup/${type.name}"
}

@Composable
fun WrxNavGraph(
    navController: NavHostController,
    repository: WrxRepository
) {
    NavHost(
        navController = navController,
        startDestination = WrxDestinations.HOME
    ) {
        composable(WrxDestinations.HOME) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.Factory(repository)
            )
            HomeScreen(
                viewModel = viewModel,
                onGenerationClick = { genId ->
                    navController.navigate(WrxDestinations.detailRoute(genId))
                },
                onQuickLookupClick = { type ->
                    navController.navigate(WrxDestinations.quickLookupRoute(type))
                }
            )
        }

        composable(
            route = WrxDestinations.DETAIL,
            arguments = listOf(navArgument("generationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val genId = backStackEntry.arguments?.getString("generationId") ?: ""
            val viewModel: DetailViewModel = viewModel(
                factory = DetailViewModel.Factory(genId, repository)
            )
            GenerationDetailScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = WrxDestinations.QUICK_LOOKUP,
            arguments = listOf(navArgument("type") { type = NavType.StringType })
        ) { backStackEntry ->
            val typeStr = backStackEntry.arguments?.getString("type") ?: QuickLookupType.TORQUE_SPECS.name
            val type = try {
                QuickLookupType.valueOf(typeStr)
            } catch (e: Exception) {
                QuickLookupType.TORQUE_SPECS
            }

            val viewModel: QuickLookupViewModel = viewModel(
                factory = QuickLookupViewModel.Factory(type, repository)
            )
            QuickLookupScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
