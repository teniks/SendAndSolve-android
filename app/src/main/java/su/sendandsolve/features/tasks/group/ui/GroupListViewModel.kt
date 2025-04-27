package su.sendandsolve.features.tasks.group.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.Repository
import su.sendandsolve.features.tasks.domain.model.Group
import su.sendandsolve.features.tasks.domain.model.Task
import java.time.Instant
import java.util.UUID

class GroupListViewModel(
    private val repository: Repository<Group>
) : ViewModel() {
    private val _groups: MutableStateFlow<List<Group>> = MutableStateFlow(emptyList())
    val groups: StateFlow<List<Group>> = _groups

    init {
        fetchTestData()
    }

    private fun fetchTestData() {
        _groups.value = createTestGroups()
    }

    fun fetchData(){
        // Подписываемся на Flow из БД
        viewModelScope.launch {
            repository
                .getAll()
                .catch { e -> println(e) } // Обработка ошибок
                .collect { groups ->
                    _groups.value = groups
                }
        }
    }

    private fun createTestGroups() = listOf(
        Group(
            uuid = UUID.randomUUID(),
            name = "Test group 1",
            tasks = (1..5).map {
                DomainState.Read
            }.associateBy {
                Task(
                    uuid = UUID.randomUUID(),
                    title = "Test task",
                    description = "Test task description",
                    status = "Test status",
                    priority = 0,
                    startDate = Instant.now(),
                    progress = 0,
                    creatorId = UUID.randomUUID(),
                    scope = "Test scope $it",
                    creationDate = Instant.now(),
                    endDate = Instant.now().plusSeconds(3600))  } as MutableMap<Task, DomainState>,

            creatorId = UUID.randomUUID(),
            criteria = mapOf("test" to "test")
        ),
        Group(
            uuid = UUID.randomUUID(),
            name = "Test group 2",
            tasks = (1..5).map {
                DomainState.Read
            }.associateBy {
                Task(
                    uuid = UUID.randomUUID(),
                    title = "Test task",
                    description = "Test task description",
                    status = "Test status",
                    priority = 0,
                    startDate = Instant.now(),
                    progress = 0,
                    creatorId = UUID.randomUUID(),
                    scope = "Test scope $it",
                    creationDate = Instant.now())  } as MutableMap<Task, DomainState>,
            creatorId = UUID.randomUUID(),
            criteria = mapOf("test" to "test")
        )
    )
}