package com.lloyds.myapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lloyds.myapp.adapters.CategoryItemAdapter
import com.lloyds.myapp.databinding.FragmentSecondBinding
import com.lloyds.myapp.datamodel.MealItem
import com.lloyds.myapp.datamodel.Meals
import com.lloyds.myapp.utils.OnItemClickListener
import com.lloyds.myapp.utils.SessionManager
import com.lloyds.myapp.utils.UniversalManager
import com.lloyds.myapp.utils.UniversalManager.getSessionManagerContext
import com.lloyds.myapp.utils.UniversalManager.moveToOtherFragment
import com.lloyds.myapp.utils.UniversalManager.moveToOtherFragmentBack
import com.lloyds.myapp.utils.UniversalManager.showToast
import com.lloyds.myapp.viewmodel.MealCategoryViewModel
import com.lloyds.myapp.viewmodel.MealViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MealCategoryFragment : Fragment(), View.OnClickListener, OnItemClickListener {

    private val TAG = "MealCategoryFragment"
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MealCategoryViewModel
    var activity: AppCompatActivity? = null
    lateinit var sessionManager: SessionManager
    private lateinit var mCategoryItemAdapter: CategoryItemAdapter
    private lateinit var mOnItemClickListener: OnItemClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connectivityRepository = UniversalManager.getInstance(requireContext())
        viewModel = MealCategoryViewModel(connectivityRepository)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        initVariable()
    }

    private fun initVariable() {
        sessionManager = getSessionManagerContext(activity as Context)
        val mealItem = sessionManager.getMealName()
        mOnItemClickListener = this

        binding.apply {
            tvHeader.text = mealItem
            btnBack.setOnClickListener(this@MealCategoryFragment)
        }
        setUpRecyclerView()
        viewModel.isOnline.observe(this.viewLifecycleOwner) { isOnline ->
            if (isOnline)
                viewModel.getMealsCategoryItem(mealItem.toString())
            else
                showToast(activity as Context, "No Internet Connection!")
        }
        //viewModel.getMealsCategoryItem(mealItem.toString())

        viewModel.responseContainer.observe(this.viewLifecycleOwner) {
            if (it != null) {
                mCategoryItemAdapter.mealItem = it.meals
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

    private fun setUpRecyclerView() = binding.recyclerTilesMealsItems.apply {
        mCategoryItemAdapter = CategoryItemAdapter(mOnItemClickListener)
        adapter = mCategoryItemAdapter
        layoutManager = LinearLayoutManager(activity as Context)
        mCategoryItemAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        if (v == binding.btnBack) {
            moveToOtherFragmentBack(this, R.id.FirstFragment)
        }
    }

    override fun onItemClick(meals: Meals, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClickItem(meals: MealItem, position: Int) {
        sessionManager.addMealItemId(meals.idMeal)
        Log.d("TAG ->>", meals.idMeal.toString())
        moveToOtherFragment(this, R.id.action_SecondFragment_to_ThirdFragment)
    }
}