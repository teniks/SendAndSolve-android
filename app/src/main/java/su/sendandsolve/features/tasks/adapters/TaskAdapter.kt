package su.sendandsolve.features.tasks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import su.sendandsolve.core.utils.DeadlineUtil
import su.sendandsolve.databinding.TasksItemTaskBinding
import su.sendandsolve.features.tasks.domain.model.Task

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private val items = mutableListOf<Task>()

    class ViewHolder(private val binding: TasksItemTaskBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task){
            binding.taskName.text = task.title
            binding.taskDescription.text = task.description
            binding.taskDeadline.text = DeadlineUtil.getString(task.startDate, task.endDate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TasksItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int =
        items.size

    private class TaskDiffCallback(
        private val oldList: List<Task>,
        private val newList: List<Task>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].uuid == newList[newItemPosition].uuid
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }

    fun submitList(newList: List<Task>) {
        val diffResult = DiffUtil.calculateDiff(TaskDiffCallback(items, newList))
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}