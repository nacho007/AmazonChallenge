package actions

import actions.utils.DomainObjectsMocks
import com.idd.domain.actions.GetNetWorkRedditEntriesAction
import com.idd.domain.models.ResultWrapper
import com.idd.domain.models.reddit.RedditResponse
import com.idd.domain.repositories.RedditNetworkRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GetNetWorkRedditEntriesTest {

    private lateinit var getRedditNetWorkEntriesAction: GetNetWorkRedditEntriesAction

    @MockK
    internal lateinit var redditNetWorkRepository: RedditNetworkRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getRedditNetWorkEntriesAction = GetNetWorkRedditEntriesAction(redditNetWorkRepository)
    }

    @Test
    fun `should return Result Success when loading network entries`() = runBlockingTest {
        //given
        val reddits = DomainObjectsMocks.getRedditResponse()
        given(reddits = reddits)

        //when
        val result = getRedditNetWorkEntriesAction(PAGE_SIZE, "")

        // then
        result shouldBeEqualTo GetNetWorkRedditEntriesAction.Result.Success(reddits)
    }

    @Test
    fun `should return Result Error when loading network entries`() = runBlockingTest {
        //given
        val reddits = DomainObjectsMocks.getRedditResponse()
        given(result = ResultWrapper.Error())

        //when
        val result = getRedditNetWorkEntriesAction(PAGE_SIZE, "")

        // then
        result shouldBeEqualTo GetNetWorkRedditEntriesAction.Result.Error()
    }

    private fun given(
        reddits: RedditResponse = DomainObjectsMocks.getRedditResponse(),
        result: ResultWrapper<RedditResponse> = ResultWrapper.Success(reddits)
    ) {
        coEvery { redditNetWorkRepository.getReddits(PAGE_SIZE, "") } returns result
    }

    companion object {
        const val PAGE_SIZE = 50
    }
}
