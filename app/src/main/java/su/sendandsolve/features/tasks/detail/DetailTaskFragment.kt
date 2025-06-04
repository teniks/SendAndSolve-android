package su.sendandsolve.features.tasks.detail

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import su.sendandsolve.R
import su.sendandsolve.core.items.users.UserAdapter
import su.sendandsolve.core.items.users.UserSelectionDialogFragment
import su.sendandsolve.databinding.TasksFragmentDetailTaskBinding
import su.sendandsolve.features.tasks.adapters.DropdownAdapter
import su.sendandsolve.features.tasks.adapters.TagAdapter
import su.sendandsolve.features.tasks.adapters.TaskAdapter
import su.sendandsolve.features.tasks.detail.tags.TagsSelectionDialogFragment
import su.sendandsolve.features.tasks.detail.tasks.TasksSelectionDialogFragment
import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.enums.TaskPriority
import su.sendandsolve.features.tasks.domain.enums.TaskStatus
import su.sendandsolve.features.tasks.domain.model.Tag
import su.sendandsolve.features.tasks.domain.model.Task
import su.sendandsolve.features.tasks.domain.model.User
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.UUID

@AndroidEntryPoint
class DetailTaskFragment : Fragment() {
    private lateinit var binding: TasksFragmentDetailTaskBinding
    private val viewModel: DetailTaskViewModel by viewModels()
    private var taskId: UUID? = null
    private lateinit var tagAdapter: TagAdapter
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var userAdapter: UserAdapter
    private lateinit var priorityAdapter: DropdownAdapter
    private lateinit var statusAdapter: DropdownAdapter

    private var onSetTaskId: (() -> UUID)? = null

    private val dateFormatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.SHORT)
        .withZone(ZoneId.systemDefault())
    private val timeFormatter = DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .withZone(ZoneId.systemDefault())

    companion object {
        fun newInstance() = DetailTaskFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskId = onSetTaskId?.invoke()

        taskId = taskId ?: UUID.randomUUID()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = TasksFragmentDetailTaskBinding.inflate(inflater, container, false)
        setupTaskAdapter()
        setupTagAdapter()
        setupUserAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadTask(taskId)

        setupAppBar()

        setupSelectedTags()
        setupChildTasks()
        setupExecutors()

        setupDropdowns(view.context)
        setupDateTimePickers()
        observeViewModel()
    }

    private fun setupAppBar() {
        with(binding) {
            val navController = findNavController()
            val barConfiguration = AppBarConfiguration(navController.graph)
            barTop.setupWithNavController(navController, barConfiguration)

            barTop.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.item_save -> {
                        viewModel.onEvent(DetailTaskEvent.TaskChanged { copy() })
                        if (viewModel.state.value.error == null && viewModel.state.value.startDateError == null) {
                            viewModel.onEvent(DetailTaskEvent.Submit)
                            Toast.makeText(context, R.string.task_was_saved, Toast.LENGTH_SHORT).show()
                            true
                        } else {
                            false
                        }
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupTagAdapter() {
        tagAdapter = TagAdapter { tag ->
            removeSelectedTag(tag)
            vibrate()
        }
    }

    private fun removeSelectedTag(tag: Tag) {
        viewModel.onEvent(DetailTaskEvent.TaskChanged { deleteTag(tag) })
    }

    private fun setupSelectedTags() {
        binding.recyclerSelectedTags.apply {
            layoutManager = LinearLayoutManager(
                this@DetailTaskFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false)
            adapter = tagAdapter
        }

        binding.chipBtnTagAdd.setOnClickListener {
            showTagSelectionDialog()
        }
    }

    private fun setupTaskAdapter() {
        val onLongClick: ((Task) -> Boolean ) = {
            removeChildTask(it)
            vibrate()
            true
        }

        taskAdapter = TaskAdapter().apply {
            setListenerOnLongClick { task ->
                onLongClick(task)
            }
        }
    }

    private fun removeChildTask(task: Task) {
        viewModel.onEvent(DetailTaskEvent.TaskChanged { deleteChildTask(task) })
    }

    private fun setupChildTasks() {
        binding.recyclerSelectedTasks.apply {
            layoutManager = LinearLayoutManager(
                this@DetailTaskFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = taskAdapter
        }

        binding.btnTaskAdd.setOnClickListener {
            showTaskSelectionDialog()
        }
    }

    private fun setupUserAdapter() {
        val onLongClick: ((User) -> Boolean) = {
            removeExecutor(it)
            vibrate()
            true
        }

        userAdapter = UserAdapter().apply {
            setListenerOnLongClick { task ->
                onLongClick(task)
            }
        }
    }

    private fun removeExecutor(user: User) {
        viewModel.onEvent(DetailTaskEvent.TaskChanged { deleteExecutor(user) })
    }

    private fun setupExecutors() {
        binding.recyclerExecutors.apply {
            layoutManager = LinearLayoutManager(
                this@DetailTaskFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = userAdapter
        }

        binding.btnUserAdd.setOnClickListener {
            showUserSelectionDialog()
        }
    }

    private fun setupDropdowns(context: Context) {
        priorityAdapter = DropdownAdapter(context, TaskPriority.getPriorityList(context)) { textView, imageView, element ->
            val existingElement = element as Pair<TaskPriority, String>
            textView.text = existingElement.second

            when (existingElement.first) {
                TaskPriority.CRITICAL -> imageView.setImageResource(R.drawable.ic_launcher_foreground)
                TaskPriority.STRATEGIC -> imageView.setImageResource(R.drawable.ic_launcher_foreground)
                TaskPriority.TACTICAL -> imageView.setImageResource(R.drawable.ic_launcher_foreground)
                TaskPriority.BACKGROUND -> imageView.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }

        binding.dropdownPriority.apply {
            setAdapter(priorityAdapter)
            setOnItemClickListener { _, _, position, _ ->
                val selected = priorityAdapter.items[position] as Pair<TaskPriority, String>
                viewModel.onEvent(DetailTaskEvent.TaskChanged { copy(priority = selected.first.ordinal) })
            }
        }

        statusAdapter = DropdownAdapter(context, TaskStatus.getStatusList(context)) { textView, imageView, element ->
            val existingElement = element as Pair<TaskStatus, String>
            textView.text = existingElement.second

            when (existingElement.first) {
                TaskStatus.NEW -> imageView.setImageResource(R.drawable.ic_launcher_foreground)
                TaskStatus.IN_PROGRESS -> imageView.setImageResource(R.drawable.ic_launcher_foreground)
                TaskStatus.ON_HOLD -> imageView.setImageResource(R.drawable.ic_launcher_foreground)
                TaskStatus.COMPLETED -> imageView.setImageResource(R.drawable.ic_launcher_foreground)
                TaskStatus.CANCELLED -> imageView.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }

        binding.dropdownStatus.apply {
            setAdapter(statusAdapter)
            setOnItemClickListener { _, _, position, _ ->
                val selected = statusAdapter.items[position] as Pair<TaskStatus, String>
                viewModel.onEvent(DetailTaskEvent.TaskChanged { copy(status = selected.first.name) })
            }
        }
    }

    private fun setupDateTimePickers() {
        val startDateEditText = binding.etStartDate
        val startTimeEditText = binding.etStartTime
        val endDateEditText = binding.etEndDate
        val endTimeEditText = binding.etEndTime

        startDateEditText.setOnClickListener { showDatePicker(true, startDateEditText) }
        startTimeEditText.setOnClickListener { showTimePicker(true, startTimeEditText) }
        endDateEditText.setOnClickListener { showDatePicker(false, endDateEditText) }
        endTimeEditText.setOnClickListener { showTimePicker(false, endTimeEditText) }


        binding.layoutEndDate.setEndIconOnClickListener {
            endDateEditText.text?.clear()
            viewModel.onEvent(DetailTaskEvent.TaskChanged { copy(endDate = null) })
        }
    }

    private fun showDatePicker(isStartDate: Boolean, parentElement: View) {
        parentElement.isClickable = false
        // Выбор даты
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.select_date)
            .build()

        datePicker.addOnPositiveButtonClickListener { selectedDateMillis ->
            if (selectedDateMillis == null) return@addOnPositiveButtonClickListener

            val selected = if (isStartDate) {
                viewModel.state.value.task?.startDate ?: Instant.now()
            } else {
                viewModel.state.value.task?.endDate ?: Instant.now()
            }
            val existingDate = selected.atZone(ZoneId.systemDefault())

            // Собираем дату и время
            val instant = Instant
                .ofEpochMilli(selectedDateMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .atTime(existingDate.hour, existingDate.minute)
                .atZone(ZoneId.systemDefault())
                .toInstant()

            // Обновить данные и UI
            if (isStartDate){
                viewModel.onEvent(DetailTaskEvent.TaskChanged { copy(startDate = instant) })
            } else {
                viewModel.onEvent(DetailTaskEvent.TaskChanged { copy(endDate = instant) })
            }

            datePicker.dismiss()
        }
        datePicker.show(parentFragmentManager, "DATE_PICKER")

        parentElement.isClickable = true
    }

    private fun showTimePicker(isStartTime: Boolean, parentElement: View) {
        parentElement.isClickable = false

        val timePicker = MaterialTimePicker.Builder()
            .setHour(23)
            .setMinute(0)
            .setTitleText(R.string.select_time)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val selected = if (isStartTime) {
                viewModel.state.value.task?.startDate ?: Instant.now()
            } else {
                viewModel.state.value.task?.endDate ?: Instant.now()
            }
            val existingDate = selected.atZone(ZoneId.systemDefault())

            // Собираем дату и время
            val instant = existingDate
                .toLocalDate()
                .atTime(timePicker.hour, timePicker.minute)
                .atZone(ZoneId.systemDefault())
                .toInstant()

            // Обновить данные и UI
            if (isStartTime) {
                viewModel.onEvent(DetailTaskEvent.TaskChanged { copy(startDate = instant) })
            } else {
                viewModel.onEvent(DetailTaskEvent.TaskChanged { copy(endDate = instant) })
            }

            timePicker.dismiss()
        }
        timePicker.show(parentFragmentManager, "TIME_PICKER")

        parentElement.isClickable = true
    }

    private fun observeViewModel() {
        viewModel.viewModelScope.launch {
            viewModel.state.collect {
                binding.layoutStartDate.error = it.startDateError
                binding.layoutEndDate.error = it.endDateError
                binding.etTaskName.setText(it.task?.title ?: "")
                binding.etStartDate.setText(it.task?.startDate?.let { date -> dateFormatter.format(date) } ?: "")
                binding.etStartTime.setText(it.task?.startDate?.let { date -> timeFormatter.format(date) } ?: "")
                binding.etEndDate.setText(it.task?.endDate?.let { date -> dateFormatter.format(date) } ?: "")
                binding.etEndTime.setText(it.task?.endDate?.let { date -> timeFormatter.format(date) } ?: "")
                binding.etTaskDescription.setText(it.task?.description ?: "")

                it.task?.status?.let { status ->
                    statusAdapter.items.find { item -> (item.first as TaskStatus).name == status }?.let { element ->
                        binding.dropdownStatus.setText(element.second, false)
                    }
                }

                it.task?.priority?.let { priority ->
                    priorityAdapter.items.find { item -> (item.first as TaskPriority).ordinal == priority }?.let { element ->
                        binding.dropdownPriority.setText(element.second, false)
                    }
                }

                it.task?.tags?.keys?.let { tags -> tagAdapter.submitList(tags.toList()) }
                it.task?.getTagsByNotState(DomainState.Delete)?.keys?.let { tags -> tagAdapter.submitList(tags.toList()) }
                it.task?.getChildTasksByNotState(DomainState.Delete)?.let { tasks ->  taskAdapter.submitList(tasks.keys.toList()) }
                it.task?.getExecutorsByNotState(DomainState.Delete)?.let { executors -> userAdapter.submitList(executors.keys.toList()) }
            }
        }
    }

    private fun vibrate() {
        val vibrator: Vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val vibratorManager: VibratorManager = requireContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        vibrator.cancel()
        vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun showTagSelectionDialog() {
        val currentTags = viewModel.state.value.task
            ?.getTagsByNotState(DomainState.Delete)
            ?.keys
            ?: emptySet()

        val dialog = TagsSelectionDialogFragment().apply {
            setListenerOnSave { selectedTags ->
                selectedTags.forEach { tag ->
                    viewModel.onEvent(DetailTaskEvent.TaskChanged { addTag(tag) })
                }
            }

            setListenerOnSetSelected {
                return@setListenerOnSetSelected currentTags
            }
        }

        dialog.show(parentFragmentManager, "TAGS_SELECTION_DIALOG")
    }

    private fun showTaskSelectionDialog() {
        val currentTasks = viewModel.state.value.task
            ?.getChildTasksByNotState(DomainState.Delete)
            ?.keys
            ?: emptySet()

        val dialog = TasksSelectionDialogFragment().apply {
            setListenerOnSave { selectedTasks ->
                selectedTasks.forEach { task ->
                    viewModel.onEvent(DetailTaskEvent.TaskChanged { addChildTask(task)})
                }
            }

            setListenerOnSetSelected {
                return@setListenerOnSetSelected currentTasks
            }
        }

        dialog.show(parentFragmentManager, "TASKS_SELECTION_DIALOG")
    }

    private fun showUserSelectionDialog() {
        val currentUsers = viewModel.state.value.task
            ?.getExecutorsByNotState(DomainState.Delete)
            ?.keys
            ?: emptySet()

        val dialog = UserSelectionDialogFragment().apply {
            setListenerOnSave { selected ->
                selected.forEach { item ->
                    viewModel.onEvent(DetailTaskEvent.TaskChanged { addExecutor(item) })
                }
            }

            setListenerOnSetSelected {
                return@setListenerOnSetSelected currentUsers
            }
        }

        dialog.show(parentFragmentManager, "EXECUTORS_SELECTION_DIALOG")
    }

    private fun setListenerOnSetTaskId(listener: () -> UUID) {
        onSetTaskId = listener
    }
}