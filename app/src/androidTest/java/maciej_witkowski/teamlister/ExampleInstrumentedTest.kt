package maciej_witkowski.teamlister


import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Rational
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.*
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule
import java.io.BufferedInputStream
import java.util.concurrent.CountDownLatch

@Rule
val rule = InstantTaskExecutorRule()

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("maciej_witkowski.teamlister", appContext.packageName)
    }
}




