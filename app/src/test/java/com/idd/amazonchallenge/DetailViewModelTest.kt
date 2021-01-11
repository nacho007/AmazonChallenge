package com.idd.amazonchallenge

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.idd.amazonchallenge.ui.details.DetailViewModel
import com.idd.domain.actions.SavePictureAction
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
class DetailViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @MockK
    internal var savePicture: SavePictureAction = mockk(relaxed = true)

    private lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = DetailViewModel(savePicture)
    }

    @Test
    fun `verify state when SavePicture returns Success`() {
        // given
        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        coEvery { savePicture(bitmap) } returns SavePictureAction.Result.Success

        // when
        viewModel.savePicture(bitmap)

        // then
        viewModel.stateLiveData.value shouldBeEqualTo DetailViewModel.ViewState(saved = true)
    }

    @Test
    fun `verify state when SavePicture returns Error`() {
        // given
        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        coEvery { savePicture(bitmap) } returns SavePictureAction.Result.Error

        // when
        viewModel.savePicture(bitmap)

        // then
        viewModel.stateLiveData.value shouldBeEqualTo DetailViewModel.ViewState(saved = false)
    }

}
