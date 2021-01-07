package com.idd.infrastructure

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
import android.content.Context
import android.graphics.Bitmap
import com.idd.domain.actions.SavePictureAction
import com.idd.infrastructure.repositories.SavePictureRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.amshove.kluent.mock
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runner.manipulation.Ordering
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class SavePictureTest {

    private lateinit var savePictureAction: SavePictureAction

    @MockK
    internal lateinit var savePictureRepository: SavePictureRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
//        savePictureRepository = SavePictureRepositoryImpl(mock(Context::class.java))
        savePictureAction = SavePictureAction(savePictureRepository)
    }

    @Test
    fun `should return Result Success when picture saved correctly in gallery`() {
        //given
        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        //when
        val result = savePictureAction(bitmap)

        //then
        result shouldBeEqualTo true
    }

    private fun given(
        booleanResult: Boolean,
        result: SavePictureAction.Result = SavePictureAction.Result.Success(booleanResult)
    ) {
//        every { userCacheRepository.saveUser(user) } returns Unit
//        coEvery { userRepository.getUser() } returns resultWrapperResult
    }

}
