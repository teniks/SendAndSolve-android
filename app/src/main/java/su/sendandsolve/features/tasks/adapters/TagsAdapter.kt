package su.sendandsolve.features.tasks.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import su.sendandsolve.R
import su.sendandsolve.databinding.CoreItemButtonBinding
import su.sendandsolve.features.tasks.domain.model.Tag
import java.util.UUID

class TagsAdapter(
    private val onAddClick: () -> Unit,
    private val onTagClick: (Tag) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Any>()
    private var selectedTagIds = emptySet<UUID>()

    inner class AddButtonViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(){
            (view as CoreItemButtonBinding).navigationBtn.text = "Создать тег"
            view.setOnClickListener { onAddClick() }
        }
    }

    inner class TagChipViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Tag, isSelected: Boolean){
            (view as Chip).apply {
                text = item.name
                isChecked = isSelected
                chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(context, if (isSelected) R.color.selected_tag else R.color.unselected_tag)
                )
            }
        }
    }

    companion object {
        const val TYPE_ERROR = 0
        const val TYPE_ADD_BUTTON = 1
        const val TYPE_TAG = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is String -> TYPE_ADD_BUTTON
            is Tag -> TYPE_TAG
            else -> TYPE_ERROR
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ADD_BUTTON -> AddButtonViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.core_item_button, parent, false))
            TYPE_TAG -> TagChipViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.tasks_item_chip, parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SelectedTagsAdapter.ViewHolder -> holder.bind(items[position] as Tag)
            is TagChipViewHolder -> {
                val tag = items[position] as Tag
                holder.bind(tag, selectedTagIds.contains(tag.uuid))
                holder.itemView.setOnClickListener { onTagClick(tag) }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newList: List<Tag>) {
        items.clear()
        items.add("add_button")
        items.addAll(newList)
        notifyDataSetChanged()
    }

    fun setSelectedTags(selectedIds: Set<UUID>) {
        selectedTagIds = selectedIds
        notifyDataSetChanged()
    }
}