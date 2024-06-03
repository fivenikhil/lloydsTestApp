package com.lloyds.myapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lloyds.myapp.databinding.FirstMealsCategoryViewBinding
import com.lloyds.myapp.datamodel.Meals
import com.lloyds.myapp.utils.OnItemClickListener

class MealCategoryAdapter(mOnItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<MealCategoryAdapter.MealsViewHolder>() {

    private var mOnItemClickListener: OnItemClickListener

    init {
        this.mOnItemClickListener = mOnItemClickListener
    }

    inner class MealsViewHolder(private val mMealsCategoryViewBinding: FirstMealsCategoryViewBinding) :
        RecyclerView.ViewHolder(mMealsCategoryViewBinding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(meals: Meals) {
            mMealsCategoryViewBinding.apply {
                textItem.text = meals.strCategory
            }
        }

        override fun onClick(p0: View?) {
            val name: Meals = meals[absoluteAdapterPosition]
            mOnItemClickListener.onItemClick(name, absoluteAdapterPosition)
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Meals>() {
        override fun areItemsTheSame(oldItem: Meals, newItem: Meals): Boolean {
            return oldItem.strCategory == newItem.strCategory
        }

        override fun areContentsTheSame(oldItem: Meals, newItem: Meals): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var meals: List<Meals>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsViewHolder {
        return MealsViewHolder(
            FirstMealsCategoryViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MealsViewHolder, position: Int) {
        holder.bind(meals[position])
    }

    override fun getItemCount() = meals.size
}