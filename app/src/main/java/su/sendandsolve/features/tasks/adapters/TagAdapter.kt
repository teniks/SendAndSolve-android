package su.sendandsolve.features.tasks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import su.sendandsolve.databinding.TasksItemChipBinding
import su.sendandsolve.features.tasks.domain.model.Tag

class TagAdapter(
    private val onTagClick: (Tag) -> Unit
) : RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    private val items: MutableList<Tag> = mutableListOf()

    inner class ViewHolder(private val chip: Chip) : RecyclerView.ViewHolder(chip) {
        fun bind(tag: Tag) {
            chip.text = tag.name
            chip.isCheckable = false
            chip.setOnLongClickListener {
                onTagClick(tag)
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TasksItemChipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)

        return ViewHolder(binding.chip)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int =
        items.size

    private class TagsDiffCallback(
        private val oldList: List<Tag>,
        private val newList: List<Tag>
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

    fun submitList(newList: List<Tag>) {
        val oldList = ArrayList(items)
        val diffResult = DiffUtil.calculateDiff(TagsDiffCallback(oldList, newList))

        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}