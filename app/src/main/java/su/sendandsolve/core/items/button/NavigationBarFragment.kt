package su.sendandsolve.core.items.button

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import su.sendandsolve.R

class NavigationBarFragment : Fragment() {

    companion object {
        fun newInstance() = NavigationBarFragment()
    }

    private val viewModel: NavigationBarViewModel by viewModels()
    private lateinit var adapter: ButtonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ButtonAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_navigation_bar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.button_recycleview).apply {
            layoutManager = LinearLayoutManager(this@NavigationBarFragment.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@NavigationBarFragment.adapter
        }

        viewModel.buttons.observe(viewLifecycleOwner) {
            adapter.apply {
                submitList(it)
            }
        }
    }
}