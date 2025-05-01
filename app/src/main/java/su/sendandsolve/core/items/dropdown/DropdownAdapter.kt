package su.sendandsolve.core.items.dropdown

import android.content.Context
import android.widget.ArrayAdapter
import su.sendandsolve.R

class DropdownAdapter(
    context: Context,
    private val items: List<DropdownItem>
) : ArrayAdapter<DropdownItem>(context, R.layout.core_item_dropdown, items) {

    override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
        val view = convertView ?: android.view.LayoutInflater.from(context)
            .inflate(R.layout.core_item_dropdown, parent, false)

        val item = getItem(position)
        item?.let {
            view.findViewById<android.widget.TextView>(R.id.dropdown_text)?.text = it.title
            view.findViewById<android.widget.ImageView>(R.id.dropdown_icon)?.setImageResource(it.iconResourceId)
        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
        return getView(position, convertView, parent)
    }
}