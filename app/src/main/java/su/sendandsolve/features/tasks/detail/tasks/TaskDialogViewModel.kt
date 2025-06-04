package su.sendandsolve.features.tasks.detail.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import su.sendandsolve.features.tasks.domain.Repository
import su.sendandsolve.features.tasks.domain.model.Task
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskDialogViewModel @Inject constructor(
    private val repository: Repository<Task>
) : ViewModel() {

    val tasks = MutableStateFlow<List<Task>>(emptyList())
    val selectedTasks = MutableStateFlow<Set<Task>>(emptySet())

    init {
        tasks.value = testData()
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
                endDate = Instant.now().plusSeconds(3600))
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            repository
                .getAll()
                .collect { t ->
                    tasks.value = t
                }
        }
    }

    fun toggleTaskSelection(task: Task) {
        selectedTasks.update { selected ->
            if (selected.contains(task)) selected - task
            else selected + task
        }
    }

    fun setSelectedTasks(tasks: Set<Task>) {
        selectedTasks.value = tasks
    }
}