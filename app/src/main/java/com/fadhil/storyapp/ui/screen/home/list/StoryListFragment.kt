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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fadhil.storyapp.R
import com.fadhil.storyapp.data.ProcessResult
import com.fadhil.storyapp.data.ProcessResultDelegate
import com.fadhil.storyapp.databinding.FragmentStoryListBinding
import com.fadhil.storyapp.domain.model.Story
import com.fadhil.storyapp.ui.screen.add.AddStoryActivity
import com.fadhil.storyapp.ui.screen.home.list.adapter.StoryAdapter
import com.fadhil.storyapp.ui.screen.home.list.adapter.StoryDelegate
import com.fadhil.storyapp.util.MarginItemDecoration
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryListFragment : Fragment() {

    private lateinit var binding: FragmentStoryListBinding
    private val viewModel: StoryListViewModel by viewModels()
    private val mStoryAdapter: StoryAdapter = StoryAdapter()

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                Snackbar.make(
                    binding.root,
                    "Upload process complete.",
                    Snackbar.LENGTH_SHORT
                ).show()
                loadData()
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
        loadData()
    }

    private fun setupView() {
        with(binding) {
            rvUser.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(false)
                addItemDecoration(
                    MarginItemDecoration(resources.getDimension(R.dimen.dimen_4dp).toInt())
                )
                adapter = mStoryAdapter
            }
        }
    }

    private fun setupListener() {
        mStoryAdapter.delegate = object : StoryDelegate {
            override fun setOnClickListener(view: View, id: String) {
                val toDetailUserFragment =
                    StoryListFragmentDirections.actionStoryListFragmentToStoryDetailFragment(
                        id
                    )
                view.findNavController().navigate(toDetailUserFragment)
            }
        }

        binding.fabAdd.setOnClickListener {
            AddStoryActivity.open(requireActivity(), resultLauncher)
        }
    }

    private fun loadData() {
        getAllStories()
    }

    private fun getAllStories() {
        viewModel.getAllStories(true)
            .observe(viewLifecycleOwner) {
                ProcessResult(it, object : ProcessResultDelegate<List<Story>?> {
                    override fun loading() {
                        showLoadIndicator()
                    }

                    override fun error(code: String?, message: String?) {
                        hideLoadIndicator()
                    }

                    override fun unAuthorize(message: String?) {
                        hideLoadIndicator()
                    }

                    override fun success(data: List<Story>?) {
                        hideLoadIndicator()
                        if (data?.isNotEmpty() == true) {
                            mStoryAdapter.setData(data)
                        }
                    }

                })
            }
    }

    private fun showLoadIndicator() {
        binding.flProgress.isVisible = true
    }

    private fun hideLoadIndicator() {
        binding.flProgress.isVisible = false
    }

}