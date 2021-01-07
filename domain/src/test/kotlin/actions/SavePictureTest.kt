package actions

import com.idd.domain.actions.SavePictureAction
import com.idd.domain.repositories.SavePictureRepository
import io.mockk.MockKAnnotations
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
class SavePictureTest {

    private lateinit var savePictureAction: SavePictureAction

    @MockK
    internal lateinit var savePictureRepository: SavePictureRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        savePictureAction = SavePictureAction(savePictureRepository)
    }

    @Test
    fun `should return Result Success when picture saved correctly in gallery`() {
        //given
        // un bitmap?


        //when
        savePictureAction(null)

//        result shouldBeEqualTo true
    }

}
