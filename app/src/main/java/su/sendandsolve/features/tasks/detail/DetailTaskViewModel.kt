package su.sendandsolve.features.tasks.detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import su.sendandsolve.R
import su.sendandsolve.core.database.room.repository.TagRepository
import su.sendandsolve.core.utils.CurrentUser
import su.sendandsolve.features.auth.utils.AuthRequiredException
import su.sendandsolve.features.tasks.domain.Repository
import su.sendandsolve.features.tasks.domain.enums.TaskPriority
import su.sendandsolve.features.tasks.domain.enums.TaskStatus
import su.sendandsolve.features.tasks.domain.model.Tag
import su.sendandsolve.features.tasks.domain.model.Task
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DetailTaskViewModel @Inject constructor(
    private val taskRepository: Repository<Task>,
    private val tagRepository: TagRepository,
    private val currentUser: CurrentUser,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(DetailTaskState())
    val state: StateFlow<DetailTaskState> = _state.asStateFlow()

    private var currentTaskId: UUID? = null

    fun onEvent(event: DetailTaskEvent) {
        when(event) {
            is DetailTaskEvent.TaskChanged -> {

                viewModelScope.launch {
                    _state.update { state ->
                        state.copy(
                            // Мы заменяем задачу копией, которую получаем из метода update.
                            task = event.update(
                                // Берем задачу из состояния или создаем новую задачу, если она отсутствует.
                                state.task ?: Task(
                                    UUID.randomUUID(),
                                    currentUser.getUserId()
                                        ?: throw AuthRequiredException("User must be authenticated")
                                )
                            )
                        )
                    }
                }

                viewModelScope.launch {
                    _state.update { state ->
                        state.copy(
                            endDateError = validateEndDate(startDate = state.task?.startDate, endDate = state.task?.endDate),
                            startDateError = validateStartDate(startDate = state.task?.startDate, endDate = state.task?.endDate)
                        )
                    }
                }

            }
            is DetailTaskEvent.Submit -> saveData()
        }
    }

    fun loadTask(taskId: UUID?) {
        viewModelScope.launch {
            try {
                val userId: UUID = currentUser.getUserId()
                    ?: throw AuthRequiredException("User must be authenticated")
                currentTaskId = taskId
                _state.update { state ->
                    state.copy(
                        task = taskId?.let { id ->
                            taskRepository.getById(id)
                        } ?: Task(UUID.randomUUID(), userId)
                    )
                }
            } catch (e: AuthRequiredException) {
                Log.e("DetailTaskViewModel", "User is not authorized", e)
                _state.update { it.copy(error = context.getString(R.string.error_auth_required)) }
            }
        }
    }

    private fun saveData(){
        viewModelScope.launch {
            _state.value.task?.let { task ->
                if(currentTaskId == null){
                    taskRepository.insert(task.copy(creationDate = Instant.now()))
                } else {
                    taskRepository.update(task)
                }

                updateTaskTags(task)
            }
        }
    }

    private suspend fun updateTaskTags(task: Task) {
        val currentTags: Flow<List<Tag>> = tagRepository.getByTask(task.uuid)

        var tagsToAdd: List<Tag>
        currentTags.collect { list ->
            tagsToAdd = task.tags.keys.filter { tag ->
                !list.any { it.uuid == tag.uuid
                }
            }
            tagsToAdd.forEach { tag ->
                tag.addTask(task)
                tagRepository.updateTasks(tag)
            }
        }

        var tagsToRemove: List<Tag>
        currentTags.collect { list ->
            tagsToRemove = list.filter { tag ->
                !task.tags.keys.any { it.uuid == tag.uuid }
            }

            tagsToRemove.forEach { tag ->
                tag.deleteTask(task)
                tagRepository.updateTasks(tag)
            }
        }
    }

    private fun validateStartDate(startDate: Instant? = null, endDate: Instant? = null): String? {
        if (startDate == null && endDate == null) return null

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            return context.getString(R.string.start_date_cannot_be_later_end)
        }
        return null
    }

    private fun validateEndDate(startDate: Instant? = null, endDate: Instant? = null): String? {
        if (startDate == null && endDate == null) return null

        if (endDate != null && startDate != null && endDate.isBefore(startDate)) {
            return context.getString(R.string.end_date_cannot_be_earlier_start)
        } else if (startDate == endDate) {
            return context.getString(R.string.end_date_cannot_be_equal_start)
        }
        return null
    }

    fun convertPriority(priority: TaskPriority): Int {
        return priority.ordinal
    }

    fun convertPriority(priority: Int): TaskPriority? {
        return TaskPriority.entries.find { it.ordinal == priority }
    }

    fun convertStatus(status: TaskStatus): String {
        return status.getStatus(context)
    }

    fun convertStatus(status: String): TaskStatus? {
        return TaskStatus.entries.find { it.getStatus(context) == status }
    }
}