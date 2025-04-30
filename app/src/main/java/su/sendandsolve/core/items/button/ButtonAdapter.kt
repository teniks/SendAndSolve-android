package su.sendandsolve.core.items.button

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import su.sendandsolve.databinding.CoreItemButtonBinding

class ButtonAdapter : RecyclerView.Adapter<ButtonAdapter.ViewHolder>() {

    private val items = mutableListOf<ButtonItem>()

    inner class ViewHolder(binding: CoreItemButtonBinding) : RecyclerView.ViewHolder(binding.root) {
        private val button: Button = binding.navigationBtn

        fun bind(item: ButtonItem) {
            button.text = item.text
            button.setOnClickListener { item.action() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CoreItemButtonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    private class ButtonDiffCallback(
        private val oldList: List<ButtonItem>,
        private val newList: List<ButtonItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
    }

    fun submitList(newList: List<ButtonItem>) {
        val oldList = ArrayList(items)
        val diffResult = DiffUtil.calculateDiff(ButtonDiffCallback(oldList, newList))
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}