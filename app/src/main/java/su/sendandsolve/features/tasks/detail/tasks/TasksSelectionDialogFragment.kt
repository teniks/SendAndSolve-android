package su.sendandsolve.features.tasks.detail.tasks

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import su.sendandsolve.R
import su.sendandsolve.databinding.TasksFragmentTasksSelectionDialogBinding
import su.sendandsolve.features.tasks.adapters.TaskSelectionAdapter
import su.sendandsolve.features.tasks.domain.model.Task

@AndroidEntryPoint
class TasksSelectionDialogFragment : DialogFragment() {
    private var _binding: TasksFragmentTasksSelectionDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskSelectionDialogViewModel by viewModels()
    private lateinit var adapter: TaskSelectionAdapter

    private var onSave: ((Set<Task>) -> Unit)? = null
    private var onSetSelected: (() -> Set<Task>)? = null

    companion object {
        fun newInstance() = TasksSelectionDialogFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = TasksFragmentTasksSelectionDialogBinding.inflate(layoutInflater)
        adapter = TaskSelectionAdapter { tag ->
            viewModel.toggleItemSelection(tag)
        }
        val selectedTasks = onSetSelected?.invoke() ?: emptySet()
        viewModel.setSelectedItems(selectedTasks)

        setupRecyclerView()

        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            builder.setTitle("Select Tasks")
                .setView(binding.root)
                .setPositiveButton(R.string.save) { _, _ ->
                    onSave?.invoke(viewModel.uiState.value.second)
                    dismiss()
                }.setNegativeButton(R.string.cancel) { _, _ ->
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart() {
        super.onStart()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.recyclerTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@TasksSelectionDialogFragment.adapter
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                adapter.submitList(state.first.map { task ->
                    TaskSelectionModel(task, state.second.contains(task))
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setListenerOnSave(listener: (Set<Task>) -> Unit) {
        onSave = listener
    }

    fun setListenerOnSetSelected(listener: () -> Set<Task>) {
        onSetSelected = listener
    }
}