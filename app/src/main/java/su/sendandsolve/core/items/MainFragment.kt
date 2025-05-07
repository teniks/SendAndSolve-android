package su.sendandsolve.core.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import su.sendandsolve.databinding.CoreFragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: CoreFragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = CoreFragmentMainBinding.inflate(inflater, container, false)
        setupButton()
        return binding.root
    }

    private fun setupButton() {

    }
}