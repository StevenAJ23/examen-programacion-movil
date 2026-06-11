package com.puce.habitatgest.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.puce.habitatgest.data.di.AppContainer
import com.puce.habitatgest.presentation.catalogo.CatalogoScreen
import com.puce.habitatgest.presentation.catalogo.CatalogoViewModel
import com.puce.habitatgest.presentation.detalle.DetalleScreen
import com.puce.habitatgest.presentation.detalle.DetalleViewModel
import com.puce.habitatgest.presentation.registro.RegistroScreen
import com.puce.habitatgest.presentation.registro.RegistroViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    container: AppContainer,
) {
    NavHost(
        navController    = navController,
        startDestination = Screen.Catalogo,
    ) {
        composable<Screen.Catalogo> {
            val vm: CatalogoViewModel = viewModel(
                factory = CatalogoViewModel.factory(container.getEspaciosUseCase),
            )
            CatalogoScreen(
                viewModel  = vm,
                onNuevo    = { navController.navigate(Screen.Registro) },
                onDetalle  = { id -> navController.navigate(Screen.Detalle(id)) },
            )
        }

        composable<Screen.Registro> {
            val vm: RegistroViewModel = viewModel(
                factory = RegistroViewModel.factory(container.saveEspacioUseCase),
            )
            RegistroScreen(
                viewModel = vm,
                onGuardado = { navController.popBackStack() },
                onCancelar = { navController.popBackStack() },
            )
        }

        composable<Screen.Detalle> { backStackEntry ->
            val route: Screen.Detalle = backStackEntry.toRoute()
            val vm: DetalleViewModel = viewModel(
                factory = DetalleViewModel.factory(
                    id                    = route.espacioId,
                    getEspaciosUseCase    = container.getEspaciosUseCase,
                    getCondicionesUseCase = container.getCondicionesUseCase,
                ),
            )
            DetalleScreen(
                viewModel = vm,
                onVolver  = { navController.popBackStack() },
            )
        }
    }
}
