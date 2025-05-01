package su.sendandsolve.core.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import su.sendandsolve.R
import su.sendandsolve.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        setupButton()
        return binding.root
    }

    private fun setupButton() {
        binding.actionBtn.setOnClickListener {
            findNavController().navigate(R.id.action_main_to_detail)
        }
    }
}