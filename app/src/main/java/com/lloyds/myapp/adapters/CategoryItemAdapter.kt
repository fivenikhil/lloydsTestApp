package com.lloyds.myapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lloyds.myapp.R
import com.lloyds.myapp.databinding.MealsCategoryItemViewBinding
import com.lloyds.myapp.datamodel.MealItem
import com.lloyds.myapp.datamodel.Meals
import com.lloyds.myapp.utils.GlideApp
import com.lloyds.myapp.utils.OnItemClickListener

class CategoryItemAdapter(mOnItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<CategoryItemAdapter.ItemViewHolder>() {

    private var mOnItemClickListener: OnItemClickListener

    init {
        this.mOnItemClickListener = mOnItemClickListener
    }

    inner class ItemViewHolder(private val mMealsCategoryItemViewBinding: MealsCategoryItemViewBinding) :
        RecyclerView.ViewHolder(mMealsCategoryItemViewBinding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(mealItem: MealItem) {
            mMealsCategoryItemViewBinding.apply {
                GlideApp.with(root.context)
                    .load(mealItem.strMealThumb)
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemImg)
                itemName.text = mealItem.strMeal
            }
        }

        override fun onClick(p0: View?) {
            val name: MealItem = mealItem[absoluteAdapterPosition]
            mOnItemClickListener.onItemClickItem(name, absoluteAdapterPosition)
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<MealItem>() {
        override fun areItemsTheSame(oldItem: MealItem, newItem: MealItem): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: MealItem, newItem: MealItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var mealItem: List<MealItem>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            MealsCategoryItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mealItem[position])
    }

    override fun getItemCount() = mealItem.size
}