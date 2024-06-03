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
import com.lloyds.myapp.adapters.MealCategoryAdapter
import com.lloyds.myapp.databinding.FragmentFirstBinding
import com.lloyds.myapp.datamodel.MealItem
import com.lloyds.myapp.datamodel.Meals
import com.lloyds.myapp.utils.NetworkChangeReceiver
import com.lloyds.myapp.utils.OnItemClickListener
import com.lloyds.myapp.utils.SessionManager
import com.lloyds.myapp.utils.UniversalManager.getInstance
import com.lloyds.myapp.utils.UniversalManager.getSessionManagerContext
import com.lloyds.myapp.utils.UniversalManager.moveToOtherFragment
import com.lloyds.myapp.utils.UniversalManager.showToast
import com.lloyds.myapp.viewmodel.MealViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MealsFragment : Fragment(), OnItemClickListener {

    private val TAG = "MealsFragment"
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MealViewModel by activityViewModels()
    var activity: AppCompatActivity? = null
    lateinit var sessionManager: SessionManager
    private lateinit var mMealCategoryAdapter: MealCategoryAdapter
    private lateinit var mOnItemClickListener: OnItemClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val connectivityRepository = getInstance(requireContext())
        //viewModel = MealViewModel(connectivityRepository)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        initVariable()
    }

    private fun initVariable() {
        sessionManager = getSessionManagerContext(activity as Context)
        mOnItemClickListener = this
        setUpRecyclerView()
        viewModel.isOnline.observe(this.viewLifecycleOwner) { isOnline ->
            if (isOnline)
                viewModel.getMealsCategory()
            else
                showToast(activity as Context, "No Internet Connection!")
        }

        viewModel.responseContainer.observe(this.viewLifecycleOwner) {
            if (it != null) {
                mMealCategoryAdapter.meals = it.meals
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

    private fun setUpRecyclerView() = binding.recyclerTilesMeals.apply {
        mMealCategoryAdapter = MealCategoryAdapter(mOnItemClickListener)
        adapter = mMealCategoryAdapter
        layoutManager = LinearLayoutManager(activity as Context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(meals: Meals, position: Int) {
        sessionManager.addMealName(meals.strCategory)
        //Log.d("TAG ->>", meals.strCategory.toString())
        moveToOtherFragment(this, R.id.action_FirstFragment_to_SecondFragment)
    }

    override fun onItemClickItem(meals: MealItem, position: Int) {
        TODO("Not yet implemented")
    }
}