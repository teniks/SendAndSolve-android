package su.sendandsolve.core.items.users

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
import su.sendandsolve.features.tasks.domain.model.User
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserSelectionDialogViewModel @Inject constructor(
    private val repository: Repository<User>
) : ViewModel() {

    private val items = MutableStateFlow<List<User>>(emptyList())
    private val selectedItems = MutableStateFlow<Set<User>>(emptySet())

    val uiState: StateFlow<Pair<List<User>, Set<User>>> = combine(
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

    private fun testData(): List<User> {
        return (1..5).map {
            User(
                UUID.randomUUID(),
                login = "testLogin",
                passwordHash = "testPass",
                salt = "testSalt",
                nickname = "testNick")
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

    fun toggleItemSelection(item: User) {
        selectedItems.update { selected ->
            if (selected.contains(item)) selected - item
            else selected + item
        }
    }

    fun setSelectedItems(tags: Set<User>) {
        selectedItems.value = tags
    }
}