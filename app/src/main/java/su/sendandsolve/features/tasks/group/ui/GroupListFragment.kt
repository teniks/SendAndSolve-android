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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import su.sendandsolve.R
import su.sendandsolve.core.database.room.RoomAppDatabase
import su.sendandsolve.core.database.room.repository.GroupRepository

class GroupListFragment : Fragment() {

    companion object {
        fun newInstance() = GroupListFragment()
    }

    private val viewModel: GroupListViewModel by viewModels {
        GroupListViewModelFactory(repository)
    }
    private lateinit var adapter: GroupAdapter
    private lateinit var repository: GroupRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = GroupAdapter()
        repository = GroupRepository(RoomAppDatabase.getAppDataBase(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_group_list, container, false)
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