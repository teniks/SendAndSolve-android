package su.sendandsolve.core.items.users

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
import su.sendandsolve.databinding.CoreFragmentUserSelectionDialogBinding
import su.sendandsolve.features.tasks.domain.model.User

@AndroidEntryPoint
class UserSelectionDialogFragment : DialogFragment() {
    private var _binding: CoreFragmentUserSelectionDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserSelectionDialogViewModel by viewModels()
    private lateinit var adapter: UserSelectionAdapter

    private var onSave: ((Set<User>) -> Unit)? = null
    private var onSetSelected: (() -> Set<User>)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = CoreFragmentUserSelectionDialogBinding.inflate(layoutInflater)
        adapter = UserSelectionAdapter { item ->
            viewModel.toggleItemSelection(item)
        }
        val selected = onSetSelected?.invoke() ?: emptySet()
        viewModel.setSelectedItems(selected)

        setupRecyclers()

        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            builder.setTitle(R.string.select_executors)
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

    private fun setupRecyclers() {
        binding.recyclerUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@UserSelectionDialogFragment.adapter
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state -> // Получаем обновления состояния
                adapter.submitList(state.first.map { item ->
                    // Конвертируем список тегов в список моделей для отображения в RecyclerView
                    // Тэг и содержится ли он в списке выбранных тегов
                    UserSelectionModel(item, state.second.contains(item))
                }) // Обновляем список
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Предотвращаем утечки памяти
    }

    fun setListenerOnSave(listener: (Set<User>) -> Unit) {
        onSave = listener
    }

    fun setListenerOnSetSelected(listener: () -> Set<User>) {
        onSetSelected = listener
    }
}