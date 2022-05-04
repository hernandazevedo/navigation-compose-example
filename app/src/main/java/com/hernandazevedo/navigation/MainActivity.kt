package com.hernandazevedo.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hernandazevedo.navigation.ui.theme.NavigationTheme

const val SHOW_VIEW_ROUTE = "showScreen?viewId="
const val SHOW_VIEW_ARG = "{viewId}"
const val ROOT_VIEW_ID = "1"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationTheme {
                val navController = rememberNavController()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "${SHOW_VIEW_ROUTE}{viewId}",
                    )
                    {
                        composable(
                            "showScreen?viewId={viewId}",
                            arguments = listOf(navArgument("viewId") { type = NavType.StringType
                                defaultValue = ROOT_VIEW_ID
                            })
                        ) { backStackEntry ->
                            val arguments = requireNotNull(backStackEntry.arguments)
                            val viewId = arguments.getString("viewId")

                                Column {
                                    Text("Viewid $viewId")
                                    Button(onClick = {
                                        buttonClickNext(navController, "showScreen?viewId=${viewId?.toInt()
                                            ?.plus(1)}")
                                    }) {
                                        Text("Next")
                                    }
                                    Button(onClick = {
                                        navigateUp(navController)
                                    }) {
                                        Text("Pop")
                                    }

                                    Button(onClick = {
                                        navigateUpTo(
                                            navController,
                                            "3"
                                        )
                                    }) {
                                        Text("PopTo 3")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    private fun buttonClickNext(navController: NavHostController, route: String) {
        navController.navigate(route)
    }

    private fun navigateUp(navController: NavHostController) {
        navController.navigateUp()
    }

    private fun navigateUpTo(navController: NavHostController, viewId: String?) {
        if (removeFromStackMatchingArg(
                navController = navController,
                arg = "viewId",
                argValue = viewId,
                inclusive = true
            )
        ) {
            navController.navigate("showScreen?viewId=${viewId}")
        }
    }

    private fun removeFromStackMatchingArg(
        navController: NavHostController,
        arg: String,
        argValue: Any?,
        inclusive: Boolean = false
    ): Boolean {
        var elementFound = false
        val removeList = mutableListOf<NavBackStackEntry>()
        for (item in navController.backQueue.reversed()) {
            if (item.destination.route == navController.graph.startDestinationRoute) {
                if (item.arguments?.getString(
                        arg
                    ) == argValue
                ) {
                    if(inclusive) {
                        removeList.add(item)
                    }
                    elementFound = true
                    break
                } else {
                    removeList.add(item)
                }
            }
        }

        if(elementFound) {
            navController.backQueue.removeAll(removeList)
        }
        return elementFound
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        NavigationTheme {
            Text("Android")
        }
    }
}