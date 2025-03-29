package su.sendandsolve.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import su.sendandsolve.R
import su.sendandsolve.data.items.ButtonItem

class ButtonAdapter(
    private val onItemClick: (ButtonItem) -> Unit
) : RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder>() {

    private val items = mutableListOf<ButtonItem>()

    class ButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.navigationBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_button, parent, false)

        return ButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val item = items[position]
        with(holder.button) {
            text = item.text
            setOnClickListener { onItemClick(item) }
        }
    }

    override fun getItemCount(): Int = items.size

    private class ButtonDiffCallback(
        private val oldList: List<ButtonItem>,
        private val newList: List<ButtonItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].action == newList[newItemPosition].action
    }

    fun submitList(newList: List<ButtonItem>) {
        val diffResult = DiffUtil.calculateDiff(ButtonDiffCallback(items, newList))
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}