package su.sendandsolve.features.tasks.detail.ui.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import su.sendandsolve.features.tasks.domain.Repository
import su.sendandsolve.features.tasks.domain.model.Tag
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TagsDialogViewModel @Inject constructor(
    private val repository: Repository<Tag>
) : ViewModel() {

    private val _tags = MutableStateFlow<List<Tag>>(emptyList())
    private val _selectedTags = MutableStateFlow<Set<UUID>>(emptySet())

    val uiState: StateFlow<TagsDialogState> = combine(
        _tags,          // Следим за тегами
        _selectedTags // И выбранными ID
    ) { tags, selected ->
        TagsDialogState(tags, selected) // Объединяем в одно состояние
    }.stateIn(
        scope = viewModelScope,  // Работает в рамках жизненного цикла ViewModel
        started = SharingStarted.WhileSubscribed(5000), // Автоматически останавливается через 5 сек без подписчиков
        initialValue = TagsDialogState()  // Начальное состояние
    )

    init {
        loadTags()
    }

    private fun loadTags() {
        viewModelScope.launch {
            repository
                .getAll()
                .collect { t ->
                    _tags.value = t
                }
        }
    }

    fun toggleTagSelection(tagId: UUID) {
        _selectedTags.update { selected ->
            if (selected.contains(tagId)) selected - tagId else selected + tagId
        }
    }

    fun createTag(name: String) {
        viewModelScope.launch {
            val newTag = Tag(UUID.randomUUID(), name)
            repository.insert(newTag)
        }
    }
}