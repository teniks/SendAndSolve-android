package su.sendandsolve.features.auth.presentation.authorization

import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import su.sendandsolve.R
import su.sendandsolve.databinding.AuthFragmentAuthorizationBinding

@AndroidEntryPoint
class AuthorizationFragment : Fragment() {

    companion object {
        fun newInstance() = AuthorizationFragment()
    }

    private val viewModel: AuthorizationViewModel by viewModels()
    private lateinit var binding: AuthFragmentAuthorizationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = AuthFragmentAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observerViewModel()
    }

    private fun setupViews() {
        with(binding) {
            val part1: String = getString(R.string.first_time)
            val part2: String = getString(R.string.register)

            tvFirstTime.text = SpannableString("$part1 $part2").apply {
                setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.white)), 0, part1.length, 0)
                setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.yellow)), part1.length, length, 0)
            }
            tvFirstTime.setOnClickListener {
                navigateToRegistrationScreen()
            }
        }
    }

    private fun observerViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    updateUI(it)
                }
            }
        }
    }

    private fun updateUI(state: AuthorizationState) {
        with(binding) {
            progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

            if (state.isSuccess) {
                navigateToMainScreen()
            }

            state.error?.let { error ->
                tvError.text = error
                tvError.visibility = View.VISIBLE
            } ?: run {
                tvError.visibility = View.GONE
            }
        }
    }

    private fun navigateToRegistrationScreen() {
        findNavController().navigate(R.id.action_authorization_to_registration)
    }

    private fun navigateToMainScreen() {
        findNavController().navigate(R.id.action_authorization_to_main)
    }
}