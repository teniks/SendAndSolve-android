package su.sendandsolve.features.tasks.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import su.sendandsolve.core.utils.DeadlineUtil
import su.sendandsolve.databinding.TasksItemTaskBinding
import su.sendandsolve.features.tasks.detail.tasks.TaskSelectionModel
import su.sendandsolve.features.tasks.domain.model.Task

class TaskSelectionAdapter(
    private val onTagSelection: (Task) -> Unit
) : RecyclerView.Adapter<TaskSelectionAdapter.ViewHolder>() {

    private val items = mutableListOf<TaskSelectionModel>()

    inner class ViewHolder(private val binding: TasksItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TaskSelectionModel) {
            with(binding) {
                taskName.text = item.task.title
                taskDescription.text = item.task.description
                taskDeadline.text = DeadlineUtil.getString(item.task.startDate, item.task.endDate)

                updateAppearance(item.isSelected)

                root.setOnClickListener {
                    val newState = !item.isSelected
                    item.isSelected = newState
                    updateAppearance(newState)
                    onTagSelection(item.task)
                }
            }
        }

        private fun updateAppearance(isSelected: Boolean) {
            with(binding) {
                if(isSelected) {
                    taskName.paintFlags = (taskName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                    taskDescription.paintFlags = (taskDescription.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                } else {
                    taskName.paintFlags = (taskName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                    taskDescription.paintFlags = (taskDescription.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TasksItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int =
        items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    private class TaskDiffCallback(
        private val oldList: List<TaskSelectionModel>,
        private val newList: List<TaskSelectionModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].task.uuid == newList[newItemPosition].task.uuid
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].task == newList[newItemPosition].task &&
                    oldList[oldItemPosition].isSelected == newList[newItemPosition].isSelected
        }

    }

    fun submitList(newList: List<TaskSelectionModel>) {
        val oldList = ArrayList(items)
        val diffResult = DiffUtil.calculateDiff(TaskDiffCallback(oldList, newList))

        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}