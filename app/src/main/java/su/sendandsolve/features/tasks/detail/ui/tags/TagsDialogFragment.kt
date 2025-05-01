package su.sendandsolve.features.tasks.detail.ui.tags

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import su.sendandsolve.R
import su.sendandsolve.databinding.TasksFragmentTagsDialogBinding
import su.sendandsolve.features.tasks.adapters.TagsAdapter
import java.util.UUID

@AndroidEntryPoint
class TagsDialogFragment : DialogFragment() {
    private var _binding: TasksFragmentTagsDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TagsDialogViewModel by viewModels()
    private lateinit var adapter: TagsAdapter

    private var onTagsSelected: ((Set<UUID>) -> Unit)? = null

    companion object {
        fun newInstance(selectedIds: Set<UUID> = emptySet()) = TagsDialogFragment().apply {
            arguments = Bundle().apply {
                putSerializable("SELECTED", selectedIds.toTypedArray())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = TasksFragmentTagsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupObservers()
        setupButtons()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupRecyclerView() {
        binding.tagsRecycler.adapter = TagsAdapter(
            onAddClick = { showCreateTagDialog() }, // Клик на "+"
            onTagClick = { tag ->
                viewModel.toggleTagSelection(tag.uuid) // Клик на тег
            }
        )

        binding.tagsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext()) // Вертикальный список
            adapter = this@TagsDialogFragment.adapter // Подключаем адаптер
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state -> // Получаем обновления состояния
                adapter.submitList(state.tags) // Обновляем список
                adapter.setSelectedTags(state.selectedTagIds) // Выделяем выбранные
            }
        }
    }

    private fun setupButtons() {
        binding.saveBtn.setOnClickListener {
            onTagsSelected?.invoke(viewModel.uiState.value.selectedTagIds) // Возвращаем выбранные ID
            dismiss() // Закрываем диалог
        }

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun showCreateTagDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Создать тег")
            .setView(R.layout.dialog_create_tag)
            .setPositiveButton("Создать") { dialog, _ ->
                val input = (dialog as AlertDialog).findViewById<TextInputEditText>(R.id.tag_name_edit_text)
                input?.text?.toString()?.let {
                    if (it.isNotBlank() && it.length > 20) {
                        viewModel.createTag(it)
                    }
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Предотвращаем утечки памяти
    }
}