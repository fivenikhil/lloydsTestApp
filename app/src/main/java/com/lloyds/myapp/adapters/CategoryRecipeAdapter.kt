package com.lloyds.myapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lloyds.myapp.R
import com.lloyds.myapp.databinding.CategoryItemRecipeViewBinding
import com.lloyds.myapp.datamodel.MealItemRecipe
import com.lloyds.myapp.utils.GlideApp
import com.lloyds.myapp.utils.UniversalManager.showClickableLink

class CategoryRecipeAdapter : RecyclerView.Adapter<CategoryRecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(private val mCategoryItemRecipeViewBinding: CategoryItemRecipeViewBinding) :
        RecyclerView.ViewHolder(mCategoryItemRecipeViewBinding.root) {

        fun bind(mealItemRecipe: MealItemRecipe) {
            mCategoryItemRecipeViewBinding.apply {
                GlideApp.with(root.context)
                    .load(mealItemRecipe.strMealThumb)
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemImg)
                itemName.text = mealItemRecipe.strMeal

                val ingredientsBuilder = StringBuilder()
                for (i in 1..20) {
                    val ingredient = mealItemRecipe.getIngredient(i)
                    if (!ingredient.isNullOrBlank()) {
                        if (ingredientsBuilder.isNotEmpty()) {
                            ingredientsBuilder.append("\n")
                        }
                        ingredientsBuilder.append(ingredient)
                    }
                }
                itemIngredientItem.text = ingredientsBuilder.toString()

                val measurementBuilder = StringBuilder()
                for (i in 1..20) {
                    val measurement = mealItemRecipe.getMeasurement(i)
                    if (!measurement.isNullOrBlank()) {
                        if (measurementBuilder.isNotEmpty()) {
                            measurementBuilder.append("\n")
                        }
                        measurementBuilder.append(measurement)
                    }
                }
                itemIngredientMeasure.text = measurementBuilder.toString()

                itemInstructions.text = mealItemRecipe.strInstructions
                if (mealItemRecipe.strTags?.isNotEmpty() == true) {
                    //itemTags.text = mealItemRecipe.strTags
                    itemTags.text = "Tags - ${mealItemRecipe.strTags}"
                }

                if (mealItemRecipe.strYoutube?.isNotEmpty() == true) {
                    val link = mealItemRecipe.strYoutube
                    val text = "Watch the video"
                    showClickableLink(itemVideo, text, link.toString())
                }

                if (mealItemRecipe.strSource?.isNotEmpty() == true) {
                    val link = mealItemRecipe.strSource
                    val text = "Check the Source Link"
                    showClickableLink(itemLink, text, link.toString())
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<MealItemRecipe>() {
        override fun areItemsTheSame(oldItem: MealItemRecipe, newItem: MealItemRecipe): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: MealItemRecipe, newItem: MealItemRecipe): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var mealItemRecipe: List<MealItemRecipe>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            CategoryItemRecipeViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(mealItemRecipe[position])
    }

    override fun getItemCount() = mealItemRecipe.size

}