package com.lloyds.myapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lloyds.myapp.adapters.CategoryRecipeAdapter
import com.lloyds.myapp.databinding.FragmentThirdBinding
import com.lloyds.myapp.datamodel.MealItem
import com.lloyds.myapp.utils.SessionManager
import com.lloyds.myapp.utils.UniversalManager
import com.lloyds.myapp.utils.UniversalManager.getSessionManagerContext
import com.lloyds.myapp.utils.UniversalManager.moveToOtherFragmentBack
import com.lloyds.myapp.utils.UniversalManager.showToast
import com.lloyds.myapp.viewmodel.CategoryRecipeViewModel
import com.lloyds.myapp.viewmodel.MealViewModel

class CategoryRecipeFragment : Fragment(), View.OnClickListener {

    private val TAG = "CategoryRecipeFragment"
    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CategoryRecipeViewModel
    lateinit var sessionManager: SessionManager
    private lateinit var mCategoryRecipeAdapter: CategoryRecipeAdapter
    var activity: AppCompatActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val connectivityRepository = UniversalManager.getInstance(requireContext())
        viewModel = CategoryRecipeViewModel(connectivityRepository)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        initVariable()
    }

    private fun initVariable() {
        sessionManager = getSessionManagerContext(activity as Context)
        val mealName = sessionManager.getMealName()
        val mealItemName = sessionManager.getMealItemId()
        binding.apply {
            tvHeader.text = mealName
            btnBack.setOnClickListener(this@CategoryRecipeFragment)
        }
        setUpRecyclerView()
        viewModel.isOnline.observe(this.viewLifecycleOwner) { isOnline ->
            if (isOnline)
                viewModel.getCategoryItemView(mealItemName.toString())
            else
                showToast(activity as Context, "No Internet Connection!")
        }
       //viewModel.getCategoryItemView(mealItemName.toString())

        viewModel.responseContainer.observe(this.viewLifecycleOwner) {
            if (it != null) {
                mCategoryRecipeAdapter.mealItemRecipe = it.meals
            } else
                showToast(activity as Context, "There is some error!")
        }

        viewModel.isShowProgress.observe(this.viewLifecycleOwner) {
            if (it)
                binding.progressBar.visibility = View.VISIBLE
            else
                binding.progressBar.visibility = View.GONE

        }

        viewModel.errorMessage.observe(this.viewLifecycleOwner) {
            showToast(activity as Context, it)
        }

        viewModel.status.observe(this.viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                showToast(activity as Context, "Unable to connect!")
            }
        }
    }

    private fun setUpRecyclerView() = binding.recyclerTilesMealsItemsView.apply {
        mCategoryRecipeAdapter = CategoryRecipeAdapter()
        adapter = mCategoryRecipeAdapter
        layoutManager = LinearLayoutManager(activity as Context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        if (v == binding.btnBack) {
            moveToOtherFragmentBack(this, R.id.SecondFragment)
        }
    }
}