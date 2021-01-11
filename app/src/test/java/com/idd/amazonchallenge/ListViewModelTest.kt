package com.idd.amazonchallenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.idd.amazonchallenge.ui.common.GenericError
import com.idd.amazonchallenge.ui.list.ListViewModel
import com.idd.amazonchallenge.utils.CoroutineRule
import com.idd.amazonchallenge.utils.DomainObjectsMocks
import com.idd.domain.actions.GetLocalRedditEntriesAction
import com.idd.domain.actions.GetNetWorkRedditEntriesAction
import com.idd.domain.models.reddit.RedditResponseDataChildrenData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */

@RunWith(JUnit4::class)
class ListViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ListViewModel

    @MockK
    internal var getLocalRedditEntriesAction: GetLocalRedditEntriesAction = mockk(relaxed = true)

    @MockK
    internal var getNetWorkRedditEntriesAction: GetNetWorkRedditEntriesAction =
        mockk(relaxed = true)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = ListViewModel(getLocalRedditEntriesAction, getNetWorkRedditEntriesAction)
    }

    @Test
    fun `verify state when GetLocalRedditEntries returns Success`() {
        // given
        val redditResponse = DomainObjectsMocks.getRedditResponse()

        val redditPostList = redditResponse.redditResponseData.children?.map { children ->
            RedditResponseDataChildrenData(
                id = children.data.id,
                title = children.data.title,
                author = children.data.author,
                created = children.data.created,
                thumbnail = children.data.thumbnail,
                url = children.data.url,
                numComments = children.data.numComments,
                readPost = false,
                isLoading = false
            )
        }?.toMutableList()

        coEvery { getLocalRedditEntriesAction() } returns GetLocalRedditEntriesAction.Result.Success(
            redditResponse
        )

        // when
        viewModel.getLocalRedditEntries()

        // then
        viewModel.stateLiveData.value shouldBeEqualTo ListViewModel.ViewState(
            redditEntries = redditPostList,
            isLoading = false,
            error = null
        )
    }

    @Test
    fun `verify state when GetLocalRedditEntries returns Failure`() {
        // given
        coEvery { getLocalRedditEntriesAction() } returns GetLocalRedditEntriesAction.Result.Error

        // when
        viewModel.getLocalRedditEntries()

        // then
        viewModel.stateLiveData.value shouldBeEqualTo ListViewModel.ViewState(
            redditEntries = null,
            isLoading = false,
            error = GenericError(null, R.string.generic_error)
        )
    }

    @Test
    fun `verify state when GetNetWorkRedditEntries returns Success`() {
        // given
        val redditResponse = DomainObjectsMocks.getRedditResponse()

        val redditPostList = redditResponse.redditResponseData.children?.map { children ->
            RedditResponseDataChildrenData(
                id = children.data.id,
                title = children.data.title,
                author = children.data.author,
                created = children.data.created,
                thumbnail = children.data.thumbnail,
                url = children.data.url,
                numComments = children.data.numComments,
                readPost = false,
                isLoading = false
            )
        }?.toMutableList()

        coEvery {
            getNetWorkRedditEntriesAction(
                PAGE_SIZE,
                null
            )
        } returns GetNetWorkRedditEntriesAction.Result.Success(
            redditResponse
        )

        // when
        viewModel.getNetWorkRedditEntries()

        // then
        viewModel.stateLiveData.value shouldBeEqualTo ListViewModel.ViewState(
            redditEntries = redditPostList,
            isLoading = false,
            error = null
        )
    }

    @Test
    fun `verify state when GetNetWorkRedditEntries returns Failure`() {
        // given
        coEvery {
            getNetWorkRedditEntriesAction(
                PAGE_SIZE,
                null
            )
        } returns GetNetWorkRedditEntriesAction.Result.Error()

        // when
        viewModel.getNetWorkRedditEntries()

        // then
        viewModel.stateLiveData.value shouldBeEqualTo ListViewModel.ViewState(
            redditEntries = null,
            isLoading = false,
            error = GenericError(null, R.string.generic_error)
        )
    }

    companion object {
        const val PAGE_SIZE = 50
    }

}
