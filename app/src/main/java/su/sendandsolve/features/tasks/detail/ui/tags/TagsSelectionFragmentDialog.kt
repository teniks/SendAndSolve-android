package su.sendandsolve.features.tasks.detail.ui.tags

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
import su.sendandsolve.databinding.TasksFragmentTagsDialogBinding
import su.sendandsolve.features.tasks.adapters.TagSelectionAdapter
import su.sendandsolve.features.tasks.domain.model.Tag

@AndroidEntryPoint
class TagsSelectionFragmentDialog : DialogFragment() {
    private var _binding: TasksFragmentTagsDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TagsDialogViewModel by viewModels()
    private lateinit var adapter: TagSelectionAdapter

    private var onSaveSelectedTags: ((Set<Tag>) -> Unit)? = null
    private var onSetSelectedTags: (() -> Set<Tag>)? = null

    companion object {
        fun newInstance(selected: Set<Tag> = emptySet()) = TagsSelectionFragmentDialog().apply {
            arguments = Bundle().apply {
                putSerializable("SELECTED", selected.toTypedArray())
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = TasksFragmentTagsDialogBinding.inflate(layoutInflater)
        adapter = TagSelectionAdapter { tag: Tag ->
            viewModel.toggleTagSelection(tag)
        }
        val selectedTags = onSetSelectedTags?.invoke() ?: emptySet()
        viewModel.setSelectedTags(selectedTags)


        setupRecyclerView()

        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            builder.setTitle(R.string.select_tags)
                .setView(binding.root)
                .setPositiveButton(R.string.save) { _, _ ->
                    onSaveSelectedTags?.invoke(viewModel.uiState.value.selectedTags) // Возвращаем выбранные ID
                    dismiss() // Закрываем диалог
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
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
        binding.recyclerTags.apply {
            layoutManager = LinearLayoutManager(requireContext()) // Вертикальный список
            adapter = this@TagsSelectionFragmentDialog.adapter // Подключаем адаптер
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state -> // Получаем обновления состояния
                adapter.submitList(state.tags.map { tag ->
                    TagSelectionModel(tag, state.selectedTags.contains(tag))
                }) // Обновляем список
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Предотвращаем утечки памяти
    }

    fun setOnSaveSelectedTagsListener(listener: (Set<Tag>) -> Unit) {
        onSaveSelectedTags = listener
    }

    fun setOnSetSelectedTagsListener(listener: () -> Set<Tag>) {
        onSetSelectedTags = listener
    }
}