package su.sendandsolve.core.items.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import su.sendandsolve.databinding.CoreItemUserBinding
import su.sendandsolve.features.tasks.domain.model.User

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private val items: MutableList<User> = mutableListOf()
    private var onClick: ((User) -> Unit)? = null
    private var onLongClick: ((User) -> Boolean)? = null

    inner class ViewHolder(private val binding: CoreItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User, onUserClick: ((User) -> Unit)?, onUserLongClick: ((User) -> Boolean)?) {
            with(binding) {
                etNicknameUser.text = item.nickname
                binding.root.setOnClickListener { onUserClick?.invoke(item)}
                binding.root.setOnLongClickListener { onUserLongClick?.invoke(item) == true}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CoreItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onClick, onLongClick)
    }

    private class UserDiffCallback(
        private val oldList: List<User>,
        private val newList: List<User>
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

    fun submitList(newList: List<User>) {
        val oldList = ArrayList(items)
        val diffResult = DiffUtil.calculateDiff(UserDiffCallback(oldList, newList))

        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setListenerOnClick(listener: (User) -> Unit) {
        onClick = listener
    }

    fun setListenerOnLongClick(listener: (User) -> Boolean) {
        onLongClick = listener
    }
}