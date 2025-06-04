package su.sendandsolve.core.items.users

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import su.sendandsolve.databinding.CoreItemUserBinding
import su.sendandsolve.features.tasks.domain.model.User

class UserSelectionAdapter(
    private val onSelection: (User) -> Unit
) : RecyclerView.Adapter<UserSelectionAdapter.ViewHolder>() {

    private val items = mutableListOf<UserSelectionModel>()

    inner class ViewHolder(private val binding: CoreItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserSelectionModel) {
            with(binding) {
                etNicknameUser.text = item.user.nickname

                updateAppearance(item.isSelected)

                root.setOnClickListener {
                    val newState = !item.isSelected
                    item.isSelected = newState
                    updateAppearance(newState)
                    onSelection(item.user)
                }
            }
        }

        private fun updateAppearance(isSelected: Boolean) {
            with(binding) {
                if (isSelected) {
                    etNicknameUser.paintFlags = (etNicknameUser.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                } else {
                    etNicknameUser.paintFlags = (etNicknameUser.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                }
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
        holder.bind(items[position])
    }

    private class UserDiffCallback(
        private val oldList: List<UserSelectionModel>,
        private val newList: List<UserSelectionModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].user.uuid == newList[newItemPosition].user.uuid
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].user == newList[newItemPosition].user &&
                    oldList[oldItemPosition].isSelected == newList[newItemPosition].isSelected
        }
    }

    fun submitList(newList: List<UserSelectionModel>) {
        val oldList = ArrayList(items)
        val diffResult = DiffUtil.calculateDiff(UserDiffCallback(oldList, newList))

        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}