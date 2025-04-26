package su.sendandsolve.features.tasks.group.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import su.sendandsolve.databinding.ItemTaskGroupBinding
import su.sendandsolve.features.tasks.adapters.TaskAdapter
import su.sendandsolve.features.tasks.domain.model.Group

class GroupAdapter : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    private val items = mutableListOf<Group>()

    companion object {
        // Общий пул для всех вложенных RecyclerView
        private val sharedViewPool = RecyclerView.RecycledViewPool()
    }

    inner class GroupViewHolder(val binding: ItemTaskGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        val taskAdapter = TaskAdapter()

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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = ItemTaskGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding){
            groupName.text = item.name
            holder.taskAdapter.submitList(item.tasks.keys.toList())
        }
    }

    override fun getItemCount(): Int = items.size

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