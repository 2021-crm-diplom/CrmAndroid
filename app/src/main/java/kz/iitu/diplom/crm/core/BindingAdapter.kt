package kz.iitu.diplom.crm.core

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kz.iitu.diplom.crm.BR

abstract class BindingRecyclerAdapter : RecyclerView.Adapter<BindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val (obj, act) = bindingForPosition(position)
        holder.bind(obj, act)
    }

    override fun getItemViewType(position: Int) = layoutIdForPosition(position)

    protected abstract fun bindingForPosition(position: Int): Pair<Any?, Any?>

    protected abstract fun layoutIdForPosition(position: Int): Int
}

class BindingViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(obj: Any?, act: Any?) {
        if(obj != null) binding.setVariable(BR.obj, obj)
        if(act != null) binding.setVariable(BR.act, act)
        binding.executePendingBindings()
    }
}