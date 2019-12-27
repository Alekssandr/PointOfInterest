package com.szczecin.pointofinterest.adapter

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.szczecin.pointofinterest.R
import com.szczecin.pointofinterest.databinding.ImageItemViewBinding
import com.szczecin.pointofinterest.databinding.RouteSuggestionItemBinding
import com.szczecin.pointofinterest.route.model.StepsRoute

class StepItemsAdapter : RecyclerView.Adapter<StepItemsAdapter.ItemViewHolder>() {

    private var stepsList: List<StepsRoute> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RouteSuggestionItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.route_suggestion_item, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return stepsList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(stepsList[position])


    fun update(items: List<StepsRoute>) {
        this.stepsList = items
        notifyDataSetChanged()
    }

    class ItemViewHolder(private val binding: RouteSuggestionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stepsRoute: StepsRoute) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.htmlInstructions.text = Html.fromHtml(stepsRoute.htmlInstructions, Html.FROM_HTML_MODE_COMPACT)
            } else {
                binding.htmlInstructions.text = Html.fromHtml(stepsRoute.htmlInstructions)
            }

            binding.distance.text = stepsRoute.distance
            binding.duration.text = stepsRoute.duration
            binding.travelMode.text = stepsRoute.travelMode
            binding.executePendingBindings()
        }
    }
}
