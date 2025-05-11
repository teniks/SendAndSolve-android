package su.sendandsolve.features.auth.presentation.registration

import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import su.sendandsolve.R
import su.sendandsolve.databinding.AuthFragmentRegistrationBinding

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private lateinit var binding: AuthFragmentRegistrationBinding
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = AuthFragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        with(binding) {
            etLogin.addTextChangedListener {
                viewModel.onEvent(RegistrationEvent.LoginChanged(it.toString()))
            }

            etPassword.addTextChangedListener {
                viewModel.onEvent(RegistrationEvent.PasswordChanged(it.toString()))
            }

            etConfirmPassword.addTextChangedListener {
                viewModel.onEvent(RegistrationEvent.ConfirmPasswordChanged(it.toString()))
            }

            etNickname.addTextChangedListener {
                viewModel.onEvent(RegistrationEvent.NicknameChanged(it.toString()))
            }

            btnRegister.setOnClickListener {
                viewModel.onEvent(RegistrationEvent.Submit)
            }

            val part1: String = getString(R.string.already_have_an_account)
            val part2: String = getString(R.string.log_in)

            tvAlreadyBe.text = SpannableString("$part1 $part2").apply {
                setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.white)), 0, part1.length, 0)
                setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.yellow)), part1.length, length, 0)
            }

            tvAlreadyBe.setOnClickListener {
                navigateToAuthorizationScreen()
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect {
                        updateUI(it)
                    }
                }
                launch {
                    viewModel.stateIsValid.collect {
                        binding.btnRegister.isEnabled = it && !viewModel.state.value.isLoading
                    }
                }
            }
        }
    }

    private fun updateUI(state: RegistrationState) {
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

            state.loginError.let { binding.etLogin.error = it }
            state.passwordError.let { binding.etPassword.error = it }
            state.nicknameError.let { binding.etNickname.error = it }
            state.confirmPasswordError.let { binding.etConfirmPassword.error = it }
        }
    }

    private fun navigateToAuthorizationScreen() {
        findNavController().navigate(R.id.action_registration_to_authorization)
    }

    private fun navigateToMainScreen() {
        findNavController().navigate(R.id.action_registration_to_main)
    }
}