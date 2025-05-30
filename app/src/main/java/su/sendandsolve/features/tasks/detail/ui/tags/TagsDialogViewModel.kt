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

    private val tags = MutableStateFlow<List<Tag>>(emptyList())
    private val selectedTags = MutableStateFlow<Set<Tag>>(emptySet())

    val uiState: StateFlow<TagsDialogState> = combine(
        tags,          // Следим за тегами
        selectedTags // И выбранными ID
    ) { tags, selected ->
        TagsDialogState(tags, selected) // Объединяем в одно состояние
    }.stateIn(
        scope = viewModelScope,  // Работает в рамках жизненного цикла ViewModel
        started = SharingStarted.WhileSubscribed(5000), // Автоматически останавливается через 5 сек без подписчиков
        initialValue = TagsDialogState()  // Начальное состояние
    )

    init {
        tags.value = testData()
    }


    private fun testData(): List<Tag> {
        return listOf(
            Tag(UUID.randomUUID(), "Важно"),
            Tag(UUID.randomUUID(), "Работа"),
            Tag(UUID.randomUUID(), "Дом")
        )
    }

    private fun loadTags() {
        viewModelScope.launch {
            repository
                .getAll()
                .collect { t ->
                    tags.value = t
                }
        }
    }

    fun toggleTagSelection(tag: Tag) {
        selectedTags.update { selected ->
            if (selected.contains(tag)) selected - tag
            else selected + tag
        }
    }

    fun createTag(name: String) {
        viewModelScope.launch {
            val newTag = Tag(UUID.randomUUID(), name)
            repository.insert(newTag)
        }
    }

    fun setSelectedTags(tags: Set<Tag>) {
        selectedTags.value = tags
    }
}