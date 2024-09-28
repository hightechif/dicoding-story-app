package com.fadhil.storyapp.ui.screen.home.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.dicoding.newsapp.utils.MainDispatcherRule
import com.dicoding.newsapp.utils.getOrAwaitValue
import com.fadhil.storyapp.domain.model.Story
import com.fadhil.storyapp.domain.usecase.IStoryUseCase
import com.fadhil.storyapp.util.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryListViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyUseCase: IStoryUseCase
    private lateinit var storyListViewModel: StoryListViewModel
    private val dummyEmptyPagedStory = DataDummy.generateDummyEmptyPagedStory()

    @Before
    fun setup() {
        storyListViewModel = StoryListViewModel(storyUseCase)
        storyListViewModel.setPage(0)
        storyListViewModel.setSize(0)
        storyListViewModel.setLocation(1)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when get story is empty`() = runTest {
        val expectedPagingData = MutableLiveData<PagingData<Story>>()
        expectedPagingData.value = PagingData.empty()
        `when`(storyUseCase.getPagingStory(0, 0, 1, true))
            .thenReturn(expectedPagingData)
        val actualStories = storyListViewModel.getStoriesPagingFlow().getOrAwaitValue()
        Assert.assertNotNull(actualStories)
        Assert.assertEquals(dummyEmptyPagedStory, actualStories)
    }

}