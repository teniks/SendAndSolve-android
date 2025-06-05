package su.sendandsolve.features.tasks.detail.tags

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
import su.sendandsolve.databinding.TasksFragmentTagsSelectionDialogBinding
import su.sendandsolve.features.tasks.domain.model.Tag

@AndroidEntryPoint
class TagsSelectionDialogFragment : DialogFragment() {
    private var _binding: TasksFragmentTagsSelectionDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TagsSelectionDialogViewModel by viewModels()
    private lateinit var adapter: TagSelectionAdapter

    private var onSave: ((Set<Tag>) -> Unit)? = null
    private var onSetSelected: (() -> Set<Tag>)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = TasksFragmentTagsSelectionDialogBinding.inflate(layoutInflater)
        adapter = TagSelectionAdapter { tag: Tag ->
            viewModel.toggleItemSelection(tag)
        }
        val selectedTags = onSetSelected?.invoke() ?: emptySet()
        viewModel.setSelectedItems(selectedTags)


        setupRecyclerView()

        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            builder.setTitle(R.string.select_tags)
                .setView(binding.root)
                .setPositiveButton(R.string.save) { _, _ ->
                    onSave?.invoke(viewModel.uiState.value.selectedTags) // Возвращаем выбранные ID
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
            adapter = this@TagsSelectionDialogFragment.adapter // Подключаем адаптер
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state -> // Получаем обновления состояния
                adapter.submitList(state.tags.map { tag ->
                    // Конвертируем список тегов в список моделей для отображения в RecyclerView
                    // Тэг и содержится ли он в списке выбранных тегов
                    TagSelectionModel(tag, state.selectedTags.contains(tag))
                }) // Обновляем список
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Предотвращаем утечки памяти
    }

    fun setListenerOnSave(listener: (Set<Tag>) -> Unit) {
        onSave = listener
    }

    fun setListenerOnSetSelected(listener: () -> Set<Tag>) {
        onSetSelected = listener
    }
}