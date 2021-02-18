package kz.iitu.diplom.crm.modules.startup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.iitu.diplom.crm.R

class WelcomePagerAdapter : RecyclerView.Adapter<WelcomePagerAdapter.ItemViewHolder>() {

    private val items = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.startup_welcome_pager_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun setItems(items: Array<String>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val welcomeTextTitleView = view.findViewById<TextView>(R.id.welcomeTextTitle)
        private val welcomeTextDescriptionView = view.findViewById<TextView>(R.id.welcomeTextDescription)

        fun bind(welcomeText: String) {
            val textParts = welcomeText.split(";")
            welcomeTextTitleView.text = textParts[0]
            welcomeTextDescriptionView.text = textParts[1]
        }
    }
}