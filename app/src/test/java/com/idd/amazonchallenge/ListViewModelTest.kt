package com.idd.amazonchallenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.idd.amazonchallenge.ui.common.GenericError
import com.idd.amazonchallenge.ui.list.ListViewModel
import com.idd.amazonchallenge.utils.DomainObjectsMocks
import com.idd.domain.actions.GetLocalRedditEntriesAction
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
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

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ListViewModel

    @MockK
    internal var getLocalRedditEntriesAction: GetLocalRedditEntriesAction = mockk(relaxed = true)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = ListViewModel(getLocalRedditEntriesAction)
    }

    @Test
    fun `verify state when GetLocalRedditEntries returns Success`() {
        // given
        val redditResponse = DomainObjectsMocks.getRedditResponse()

        coEvery { getLocalRedditEntriesAction() } returns GetLocalRedditEntriesAction.Result.Success(
            redditResponse
        )

        // when
        viewModel.getLocalRedditEntries(false)

        // then
        viewModel.stateLiveData.value shouldBeEqualTo ListViewModel.ViewState(
            redditEntries = redditResponse,
            isRefreshing = false,
            isLoading = false,
            error = null
        )
    }

    @Test
    fun `verify state when GetLocalRedditEntries returns Failure`() {
        // given
        coEvery { getLocalRedditEntriesAction() } returns GetLocalRedditEntriesAction.Result.Error

        // when
        viewModel.getLocalRedditEntries(false)

        // then
        viewModel.stateLiveData.value shouldBeEqualTo ListViewModel.ViewState(
            redditEntries = null,
            isRefreshing = false,
            isLoading = false,
            error = GenericError(null, R.string.generic_error)
        )
    }

}
