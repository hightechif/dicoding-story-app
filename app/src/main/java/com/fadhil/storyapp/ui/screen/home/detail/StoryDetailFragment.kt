package com.fadhil.storyapp.ui.screen.home.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.fadhil.storyapp.data.ProcessResult
import com.fadhil.storyapp.data.ProcessResultDelegate
import com.fadhil.storyapp.databinding.FragmentStoryDetailBinding
import com.fadhil.storyapp.domain.model.Story
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryDetailFragment : Fragment() {
    private lateinit var binding: FragmentStoryDetailBinding
    private val viewModel: StoryDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObserver()
        setupListener()
    }


    override fun onStart() {
        super.onStart()

        loadData()
    }

    private fun setupView() {

    }

    private fun setupObserver() {
        viewModel.isFavorite.observe(viewLifecycleOwner) {

        }
    }

    private fun setupListener() {
        binding.btnShare.setOnClickListener {
            share()
        }
    }

    private fun loadData() {
        val id = StoryDetailFragmentArgs.fromBundle(arguments as Bundle).id
        viewModel.setStoryId(id)
        getStoryDetail(id)
    }

    private fun share() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(
            Intent.EXTRA_TEXT,
            String.format("https://www.github.com/%s", binding.tvUsername.text.toString())
        )
        startActivity(Intent.createChooser(sharingIntent, "Share github user"))
    }

    private fun getStoryDetail(storyId: String) {
        viewModel.getStoryDetail(storyId, true).observe(viewLifecycleOwner) {
            ProcessResult(it, object : ProcessResultDelegate<Story?> {
                override fun loading() {
                    showLoadIndicator()
                }

                override fun error(code: String?, message: String?) {
                    hideLoadIndicator()
                }

                override fun unAuthorize(message: String?) {
                    hideLoadIndicator()
                }

                override fun success(data: Story?) {
                    hideLoadIndicator()
                    if (data != null) {
                        processData(data)
                    }
                }
            })
        }
    }

    private fun processData(data: Story) {
        viewModel.setAvatarUrl(data.photoUrl)
        with(binding) {
            tvName.text = data.name
            tvUsername.text = data.description
            Glide.with(requireContext()).load(data.photoUrl).into(imgAvatar)
        }
    }

    private fun showLoadIndicator() {
        binding.flProgress.isVisible = true
    }

    private fun hideLoadIndicator() {
        binding.flProgress.isVisible = false
    }
}