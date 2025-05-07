package su.sendandsolve.features.auth.presentation.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

            etNickname.addTextChangedListener {
                viewModel.onEvent(RegistrationEvent.NicknameChanged(it.toString()))
            }

            btnRegister.setOnClickListener {
                viewModel.onEvent(RegistrationEvent.Submit)
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    updateUI(it)
                }
            }
        }
    }

    private fun updateUI(state: RegistrationState) {
        with(binding) {
            progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            btnRegister.isEnabled = !state.isLoading

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

    private fun navigateToMainScreen() {
        findNavController().navigate(R.id.action_registration_to_main)
    }
}