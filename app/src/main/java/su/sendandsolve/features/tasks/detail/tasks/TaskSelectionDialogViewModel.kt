package su.sendandsolve.features.tasks.detail.tasks

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
import su.sendandsolve.features.tasks.domain.model.Task
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskSelectionDialogViewModel @Inject constructor(
    private val repository: Repository<Task>
) : ViewModel() {

    private val items = MutableStateFlow<List<Task>>(emptyList())
    private val selectedItems = MutableStateFlow<Set<Task>>(emptySet())

    val uiState: StateFlow<Pair<List<Task>, Set<Task>>> = combine(
        items,
        selectedItems
    ) { items, selected ->
        items to selected // Объединяем в одно состояние
    }.stateIn(
        scope = viewModelScope, // Работает в рамках жизненного цикла ViewModel
        started = SharingStarted.WhileSubscribed(5000), // Автоматически останавливается через 5 сек без подписчиков
        initialValue = Pair(emptyList(), emptySet()) // Начальное состояние
    )

    init {
        items.value = testData()
    }

    private fun testData(): List<Task> {
        return (1..5).map {
            Task(
                uuid = UUID.randomUUID(),
                title = "Test task",
                description = "Test task description",
                status = "Test status",
                priority = 0,
                startDate = Instant.now(),
                progress = 0,
                creatorId = UUID.randomUUID(),
                creationDate = Instant.now(),
                endDate = Instant.now().plusSeconds(3600 * it.toLong()))
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            repository
                .getAll()
                .collect { t ->
                    items.value = t
                }
        }
    }

    fun toggleItemSelection(item: Task) {
        selectedItems.update { selected ->
            if (selected.contains(item)) selected - item
            else selected + item
        }
    }

    fun setSelectedItems(items: Set<Task>) {
        selectedItems.value = items
    }
}