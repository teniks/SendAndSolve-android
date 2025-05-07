package su.sendandsolve

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import su.sendandsolve.databinding.ActivityMainBinding
import su.sendandsolve.features.auth.presentation.AuthCheckViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: AuthCheckViewModel by viewModels()
    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.navHostFragment.viewTreeObserver.addOnPreDrawListener { isReady }

        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when(state) {
                    is AuthCheckViewModel.AuthState.Authenticated -> {
                        isReady = true
                    }
                    is AuthCheckViewModel.AuthState.Unauthenticated -> {
                        navController.navigate(R.id.action_main_to_registration)
                        isReady = true
                    }
                    is AuthCheckViewModel.AuthState.Error -> {
                        AlertDialog.Builder(this@MainActivity)
                            .setMessage(state.message)
                            .setPositiveButton(getString(R.string.error_positive_button)) { _, _ -> viewModel.checkAuth() }
                            .setNegativeButton(getString(R.string.error_negative_button)) { _, _ ->  finish() }
                            .show()
                    }
                    is AuthCheckViewModel.AuthState.Loading -> {}
                }
            }
        }
    }
}