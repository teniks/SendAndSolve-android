package su.sendandsolve.features.tasks.detail.tags

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import su.sendandsolve.R
import su.sendandsolve.features.tasks.domain.model.Tag

class TagSelectionAdapter(
    private val onTagSelection: (Tag) -> Unit
) : RecyclerView.Adapter<TagSelectionAdapter.TagChipViewHolder>() {

    // Теги с обозначением, выбран тег или нет
    private val items = mutableListOf<TagSelectionModel>()

    // Chip тег, который меняет цвет от состояния избранности
    inner class TagChipViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val chip: Chip = view.findViewById(R.id.chip)

        fun bind(item: TagSelectionModel) {
            (chip).apply {
                text = item.tag.name

                updateAppearance(item.isSelected)

                setOnClickListener {
                    val newState = !isSelected
                    item.isSelected = newState
                    updateAppearance(newState)

                    onTagSelection(item.tag)
                }
            }
        }

        private fun updateAppearance(isSelected: Boolean) {
            chip.isChecked = isSelected

            if (isSelected) {
                chip.paintFlags = (chip.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
            } else {
                chip.paintFlags = (chip.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
            }

            chip.chipBackgroundColor = ColorStateList.valueOf(
                ContextCompat.getColor(chip.context, if (isSelected) R.color.selected_tag else R.color.unselected_tag)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagChipViewHolder {
        return TagChipViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.tasks_item_chip, parent, false))
    }

    override fun onBindViewHolder(holder: TagChipViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    private class TagsDiffCallback(
        private val oldList: List<TagSelectionModel>,
        private val newList: List<TagSelectionModel>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].tag.uuid == newList[newItemPosition].tag.uuid
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].tag.uuid == newList[newItemPosition].tag.uuid &&
                    oldList[oldItemPosition].isSelected == newList[newItemPosition].isSelected
        }
    }

    fun submitList(newList: List<TagSelectionModel>) {
        val oldList = ArrayList(items)
        val diffResult = DiffUtil.calculateDiff(TagsDiffCallback(oldList, newList))

        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}