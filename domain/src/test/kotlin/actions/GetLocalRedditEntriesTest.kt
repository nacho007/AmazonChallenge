package actions

import actions.utils.DomainObjectsMocks
import com.idd.domain.actions.GetLocalRedditEntriesAction
import com.idd.domain.models.RedditResponse
import com.idd.domain.models.ResultWrapper
import com.idd.domain.repositories.RedditLocalRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
@RunWith(JUnit4::class)
class GetLocalRedditEntriesTest {

    private lateinit var getRedditLocalEntriesAction: GetLocalRedditEntriesAction

    @MockK
    internal lateinit var redditLocalRepository: RedditLocalRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getRedditLocalEntriesAction = GetLocalRedditEntriesAction(redditLocalRepository)
    }

    @Test
    fun `should return Result Success when loading local entries`() {
        //given
        val reddits = DomainObjectsMocks.getRedditResponse()
        given(reddits = reddits)

        //when
        val result = getRedditLocalEntriesAction()

        // then
        result shouldBeEqualTo GetLocalRedditEntriesAction.Result.Success(reddits)
    }

    @Test
    fun `should return Result Error when loading local entries`() {
        //given
        val reddits = DomainObjectsMocks.getRedditResponse()
        given(result = ResultWrapper.Error())

        //when
        val result = getRedditLocalEntriesAction()

        // then
        result shouldBeEqualTo GetLocalRedditEntriesAction.Result.Error
    }

    private fun given(
        reddits: RedditResponse = DomainObjectsMocks.getRedditResponse(),
        result: ResultWrapper<RedditResponse> = ResultWrapper.Success(reddits)
    ) {
        every { redditLocalRepository.getReddits() } returns result
    }

}
