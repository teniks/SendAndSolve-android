package su.sendandsolve.features.tasks.group.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import su.sendandsolve.R
import su.sendandsolve.features.tasks.adapters.GroupAdapter

@AndroidEntryPoint
class GroupListFragment : Fragment() {

    companion object {
        fun newInstance() = GroupListFragment()
    }

    private val viewModel: GroupListViewModel by viewModels()

    private lateinit var adapter: GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = GroupAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.tasks_fragment_group_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.group_list_recycleview).apply {
            layoutManager = LinearLayoutManager(this@GroupListFragment.context)
            adapter = this@GroupListFragment.adapter
        }

        viewModel.groups.onEach { groups ->
            adapter.apply {
                submitList(groups)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }
}