package su.sendandsolve.features.tasks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import su.sendandsolve.databinding.TasksItemTaskGroupBinding
import su.sendandsolve.features.tasks.domain.model.Group

class GroupAdapter : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    private val items = mutableListOf<Group>()

    companion object {
        // Общий пул для всех вложенных RecyclerView
        private val sharedViewPool = RecyclerView.RecycledViewPool()
    }

    inner class ViewHolder(private val binding: TasksItemTaskGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        private val taskAdapter = TaskAdapter()

        init {
            binding.tasksRecycleview.apply {
                // Настройка пула переиспользования ViewHolder. Предотвращает конфликты при одновременной прокрутке нескольких горизонтальных списков
                setRecycledViewPool(sharedViewPool)
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                // Отключает взаимодействие прокрутки вложенного списка с родительским. Позволяет независимо скроллить горизонтальные списки задач
                isNestedScrollingEnabled = false
                adapter = taskAdapter
            }
        }

        fun bind(group: Group){
            binding.groupName.text = group.name
            taskAdapter.submitList(group.tasks.keys.toList())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TasksItemTaskGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int =
        items.size

    private class GroupDiffCallback(
        private val oldList: List<Group>,
        private val newList: List<Group>
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

    fun submitList(newList: List<Group>) {
        val diffResult = DiffUtil.calculateDiff(GroupDiffCallback(items, newList))
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}