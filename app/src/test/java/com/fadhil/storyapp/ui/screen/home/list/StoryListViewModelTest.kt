package com.fadhil.storyapp.ui.screen.home.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.fadhil.storyapp.domain.model.Story
import com.fadhil.storyapp.domain.usecase.IStoryUseCase
import com.fadhil.storyapp.ui.screen.home.list.adapter.StoryComparator
import com.fadhil.storyapp.util.DataDummy
import com.fadhil.storyapp.utils.MainDispatcherRule
import com.fadhil.storyapp.utils.getOrAwaitValue
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
    private lateinit var viewModel: StoryListViewModel
    private val dummyValidPagedStory = DataDummy.generateDummyValidStory()
    private val dummyEmptyPagedStory = DataDummy.generateDummyEmptyStory()
    private val updateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {

        }

        override fun onRemoved(position: Int, count: Int) {

        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {

        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {

        }

    }

    @Before
    fun setup() {
        viewModel = StoryListViewModel(storyUseCase)
        viewModel.setPage(0)
        viewModel.setSize(10)
        viewModel.setLocation(1)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `get valid paging data story`() = runTest {
        val expectedPagingData = MutableLiveData<PagingData<Story>>()
        expectedPagingData.value = PagingData.from(dummyValidPagedStory)
        `when`(
            storyUseCase.getPagingStory(
                viewModel.page.value,
                viewModel.size.value,
                viewModel.location.value,
                true
            )
        ).thenReturn(expectedPagingData)
        val pagedStoryResponse = viewModel.getStoriesPaging().getOrAwaitValue()
        val asyncPagingDataDiffer = AsyncPagingDataDiffer(
            diffCallback = StoryComparator,
            updateCallback = updateCallback
        )
        asyncPagingDataDiffer.submitData(pagedStoryResponse)
        Assert.assertNotNull(pagedStoryResponse)
        Assert.assertEquals(dummyValidPagedStory.size, asyncPagingDataDiffer.snapshot().size)
        Assert.assertEquals(
            dummyValidPagedStory.first(),
            asyncPagingDataDiffer.snapshot().items.first()
        )
    }

    @Test
    fun `get story is empty when size is 0`() = runTest {
        viewModel.setSize(0)
        val expectedPagingData = MutableLiveData<PagingData<Story>>()
        expectedPagingData.value = PagingData.from(dummyEmptyPagedStory)
        `when`(
            storyUseCase.getPagingStory(
                viewModel.page.value,
                viewModel.size.value,
                viewModel.location.value,
                true
            )
        ).thenReturn(expectedPagingData)
        val pagedStoryResponse = viewModel.getStoriesPaging().getOrAwaitValue()
        val asyncPagingDataDiffer = AsyncPagingDataDiffer(
            diffCallback = StoryComparator,
            updateCallback = updateCallback
        )
        asyncPagingDataDiffer.submitData(pagedStoryResponse)
        Assert.assertNotNull(pagedStoryResponse)
        Assert.assertEquals(dummyEmptyPagedStory.size, asyncPagingDataDiffer.snapshot().size)
    }

}