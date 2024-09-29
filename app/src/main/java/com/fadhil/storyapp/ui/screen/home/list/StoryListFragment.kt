package com.fadhil.storyapp.ui.screen.home.list

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fadhil.storyapp.R
import com.fadhil.storyapp.databinding.FragmentStoryListBinding
import com.fadhil.storyapp.ui.screen.add.AddStoryActivity
import com.fadhil.storyapp.ui.screen.home.list.adapter.PagingStoryAdapter
import com.fadhil.storyapp.ui.screen.home.list.adapter.StoryComparator
import com.fadhil.storyapp.ui.screen.home.list.adapter.StoryDelegate
import com.fadhil.storyapp.ui.screen.maps.StoryMapsActivity
import com.fadhil.storyapp.util.MarginItemDecoration
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoryListFragment : Fragment() {

    private lateinit var binding: FragmentStoryListBinding
    private val viewModel: StoryListViewModel by viewModels()
    private val mStoryPagingAdapter = PagingStoryAdapter(StoryComparator)

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                Snackbar.make(
                    binding.root,
                    "Upload process complete.",
                    Snackbar.LENGTH_SHORT
                ).show()
                initData()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupListener()
        setupObserver()
        initData()
    }

    private fun setupView() {
        with(binding) {
            rvUser.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(false)
                addItemDecoration(
                    MarginItemDecoration(resources.getDimension(R.dimen.dimen_4dp).toInt())
                )
                adapter = mStoryPagingAdapter
            }
        }
    }

    private fun setupListener() {
        val callback = object : StoryDelegate {
            override fun setOnClickListener(view: View, id: String) {
                val toDetailUserFragment =
                    StoryListFragmentDirections.actionStoryListFragmentToStoryDetailFragment(
                        id
                    )
                view.findNavController().navigate(toDetailUserFragment)
            }
        }
        mStoryPagingAdapter.delegate = callback

        binding.fabAdd.setOnClickListener {
            AddStoryActivity.open(requireActivity(), resultLauncher)
        }

        binding.fabMap.setOnClickListener {
            StoryMapsActivity.open(requireActivity())
        }
    }

    private fun setupObserver() {

    }

    private fun initData() {
        viewModel.setPage(0)
        viewModel.setSize(10)
        viewModel.setLocation(1)
        getPagingStory()
    }

    private fun getPagingStory() {
        // Activities can use lifecycleScope directly; fragments use
        // viewLifecycleOwner.lifecycleScope.
        viewModel.getStoriesPaging().observe(viewLifecycleOwner) { pagingData ->
            lifecycleScope.launch {
                mStoryPagingAdapter.submitData(pagingData)
            }
        }
    }

    private fun showLoadIndicator() {
        binding.flProgress.isVisible = true
    }

    private fun hideLoadIndicator() {
        binding.flProgress.isVisible = false
    }

}