package su.sendandsolve.features.tasks.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import su.sendandsolve.R

class DropdownAdapter(
    context: Context,
    val items: List<Pair<*, String>>,
    private val bindingView: (textView: TextView, imageView: ImageView, element: Pair<*, String>) -> Unit
) : ArrayAdapter<String>(context, R.layout.core_item_dropdown, items.map { it.second } ) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.core_item_dropdown, parent, false)

        val textView = view.findViewById<TextView>(R.id.dropdown_text)
        val imageView = view.findViewById<ImageView>(R.id.dropdown_icon)
        bindingView(textView, imageView, items[position])
        return view
    }
}