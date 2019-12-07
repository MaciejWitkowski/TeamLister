package maciej_witkowski.teamlister

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Rational
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.*
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import maciej_witkowski.teamlister.utils.ImageUtils
import maciej_witkowski.teamlister.vievmodel.TeamsViewModel
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import java.io.BufferedInputStream
import java.util.concurrent.CountDownLatch



@RunWith(AndroidJUnit4::class)
class EndToEndLogicTest {

    @Test
    fun slask_1_2019_2020Test() {
        val team1Expected="27 Matus Putnocky\r\n" +
                "28 Lukasz Broz\r\n" +
                "14 Wojciech Golla\r\n" +
                "3 Piotr Celeban\r\n" +
                "4 Dino Stiglec\r\n" +
                "29 Krzysztof Maczynski\r\n" +
                "6 Michal Chrapek\r\n" +
                "8 Przemyslaw Placheta\r\n" +
                "7 Robert Pich\r\n" +
                "18 Lubambo Musonda\r\n" +
                "11 Mateusz Cholewiak\r\n" +
                "22 Daniel Kajzer\r\n" +
                "17 Mariusz Pawelec\r\n" +
                "30 Kamil Dankowski\r\n" +
                "20 Mateusz Holownia\r\n" +
                "21 Jakub Labojko\r\n" +
                "25 Damian Gaska\r\n" +
                "33 Adrian Lyszczarz\r\n" +
                "31 Maciej Palaszewski\r\n" +
                "9 Erik Exposito"
        var team1Actual:String?=null
        val team2Expected ="26 Frantisek Plach\r\n" +
                "2 Mikkel Kirkeskov\r\n" +
                "4 Jakub Czerwinski\r\n" +
                "28 Bartosz Rymaniak\r\n" +
                "88 Uros Korun\r\n" +
                "18 Patryk Sokolowski\r\n" +
                "20 Martin Konczkowski\r\n" +
                "11 Jorge Felix\r\n" +
                "10 Patryk Dziczek\r\n" +
                "21 Gerard Badia\r\n" +
                "9 Piotr Parzyszek\r\n" +
                "1 Jakub Szmatula\r\n" +
                "5 Marcin Pietrowski\r\n" +
                "6 Thomas Hateley\r\n" +
                "7 Aleksander Jagiello\r\n" +
                "14 Jakub Holubek\r\n" +
                "19 Sebastian Milewski\r\n" +
                "22 Tomasz Mokwa\r\n" +
                "29 Remigiusz Borkala\r\n" +
                "71 Dominik Steczyk"

        var team2Actual:String?=null
        val latch = CountDownLatch(2)
        var isFirstChange = true
        val testOwner = TestLifecycleOwner()
        testOwner.handleEvent(Lifecycle.Event.ON_CREATE)
        testOwner.handleEvent(Lifecycle.Event.ON_START)
        testOwner.handleEvent(Lifecycle.Event.ON_RESUME)


        val stream = Thread.currentThread().contextClassLoader.getResourceAsStream("slask_1_2019_2020.jpg")
        val bufferedInputStream = BufferedInputStream(stream)
        val bitmapTmp = BitmapFactory.decodeStream(bufferedInputStream);
        val exif = ExifInterface(stream)
        val bitmap = bitmapFromBitmap(bitmapTmp, exif)

        val app = ApplicationProvider.getApplicationContext<Application>()

        val viewModel = TeamsViewModel(app, handle = SavedStateHandle())
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.setImage(bitmap)
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.rawTeam2.observe(testOwner, Observer { value ->
                if (isFirstChange) {
                    isFirstChange = false
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(100)
                        viewModel.acceptResult()
                    }
                }
            })
            viewModel.team1.observe(testOwner, Observer { value ->
                team1Actual=value
                latch.countDown()
            })
            viewModel.team2.observe(testOwner, Observer { value ->
                team2Actual=value
                latch.countDown()  })
        }
        latch.await()

        //Long strings sometimes are displayed incorrectly in test summary
        val team1ListActual =team1Actual?.split("\r\n")
        val team1ListExpected= team1Expected.split("\r\n")
        val team2ListActual =team2Actual?.split("\r\n")
        val team2ListExpected= team2Expected.split("\r\n")

        assertEquals(team1ListExpected, team1ListActual)
        assertEquals(team2ListExpected, team2ListActual)
    }

    @Test
    fun uefaPolandIsraelTest() {
        val team1Expected ="22 Lukasz Fabianski\r\n" +
                "5 Jan Bednarek\r\n" +
                "9 Robert Lewandowski\r\n" +
                "10 Grzegorz Krychowiak\r\n" +
                "11 Kamil Grosicki\r\n" +
                "14 Mateusz Klich\r\n" +
                "15 Kamil Glik\r\n" +
                "16 Tomasz Kedziora\r\n" +
                "18 Bartosz Bereszynski\r\n" +
                "20 Piotr Zielinski\r\n" +
                "23 Krzysztof Piatek\r\n" +
                "1 Rafal Gikiewicz\r\n" +
                "12 Lukasz Skorupski\r\n" +
                "2 Michal Pazdan\r\n" +
                "3 Artur Jedrzejczyk\r\n" +
                "4 Thiago Cionek\r\n" +
                "6 Jacek Goralski\r\n" +
                "7 Arkadiusz Milik\r\n" +
                "8 Karol Linetty\r\n" +
                "13 Maciej Rybus\r\n" +
                "17 Damian Kadzior\r\n" +
                "19 Damian Szymanski\r\n" +
                "21 Przemyslaw Frankowski"
        var team1Actual: String? = null
        val team2Expected ="1 Ariel Harush\r\n" +
                "2 Eli Dasa\r\n" +
                "4 Nir Bitton\r\n" +
                "6 Bibras Natcho\r\n" +
                "7 Eran Zahavi\r\n" +
                "11 Manor Solomon\r\n" +
                "12 Sheran Yeini\r\n" +
                "15 Dor Peretz\r\n" +
                "17 Loai Taha\r\n" +
                "20 Omri Ben Harush\r\n" +
                "21 Beram Kayal\r\n" +
                "18 Ofir Marciano\r\n" +
                "23 Yoav Jarafi\r\n" +
                "3 Dan Glazer\r\n" +
                "5 Ayad Habashi\r\n" +
                "8 Almog Cohen\r\n" +
                "9 Eylon Almog\r\n" +
                "10 Dia Seba\r\n" +
                "13 Taleb Tawatha\r\n" +
                "14 Ben Sahar\r\n" +
                "16 Yonatan Cohen\r\n" +
                "19 Orel Dgani\r\n" +
                "22 Hatem Elhamed"
        var team2Actual: String? = null
        val latch = CountDownLatch(2)
        var isFirstChange = true
        val testOwner = TestLifecycleOwner()
        testOwner.handleEvent(Lifecycle.Event.ON_CREATE)
        testOwner.handleEvent(Lifecycle.Event.ON_START)
        testOwner.handleEvent(Lifecycle.Event.ON_RESUME)


        val stream = Thread.currentThread().contextClassLoader.getResourceAsStream("uefa_poland_israel_1.jpg")
        val bufferedInputStream = BufferedInputStream(stream)
        val bitmapTmp = BitmapFactory.decodeStream(bufferedInputStream);
        val exif = ExifInterface(stream)
        val bitmap = bitmapFromBitmap(bitmapTmp, exif)

        val app = ApplicationProvider.getApplicationContext<Application>()

        val viewModel = TeamsViewModel(app, handle = SavedStateHandle())
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.setImage(bitmap)
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.rawTeam2.observe(testOwner, Observer { value ->
                if (isFirstChange) {
                    isFirstChange = false
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(100)
                        viewModel.acceptResult()
                    }
                }
            })
            viewModel.team1.observe(testOwner, Observer { value ->
                team1Actual = value
                latch.countDown()
            })
            viewModel.team2.observe(testOwner, Observer { value ->
                team2Actual = value
                latch.countDown()
            })
        }
        latch.await()

        //Long strings sometimes are displayed incorrectly in test summary
        val team1ListActual = team1Actual?.split("\r\n")
        val team1ListExpected = team1Expected.split("\r\n")
        val team2ListActual = team2Actual?.split("\r\n")
        val team2ListExpected = team2Expected.split("\r\n")

        assertEquals(team1ListExpected, team1ListActual)
        assertEquals(team2ListExpected, team2ListActual)

    }
   /* @Test
    fun chelseaBournemouthTest() {
        val team1Expected ="22 Lukasz Fabianski\r\n" +
                "5 Jan Bednarek\r\n" +
                "9 Robert Lewandowski\r\n" +
                "10 Grzegorz Krychowiak\r\n" +
                "11 Kamil Grosicki\r\n" +
                "14 Mateusz Klich\r\n" +
                "15 Kamil Glik\r\n" +
                "16 Tomasz Kedziora\r\n" +
                "18 Bartosz Bereszynski\r\n" +
                "20 Piotr Zielinski\r\n" +
                "23 Krzysztof Piatek\r\n" +
                "1 Rafal Gikiewicz\r\n" +
                "12 Lukasz Skorupski\r\n" +
                "2 Michal Pazdan\r\n" +
                "3 Artur Jedrzejczyk\r\n" +
                "4 Thiago Cionek\r\n" +
                "6 Jacek Goralski\r\n" +
                "7 Arkadiusz Milik\r\n" +
                "8 Karol Linetty\r\n" +
                "13 Maciej Rybus\r\n" +
                "17 Damian Kadzior\r\n" +
                "19 Damian Szymanski\r\n" +
                "21 Przemyslaw Frankowski"
        var team1Actual: String? = null
        val team2Expected ="1 Ariel Harush\r\n" +
                "2 Eli Dasa\r\n" +
                "4 Nir Bitton\r\n" +
                "6 Bibras Natcho\r\n" +
                "7 Eran Zahavi\r\n" +
                "11 Manor Solomon\r\n" +
                "12 Sheran Yeini\r\n" +
                "15 Dor Peretz\r\n" +
                "17 Loai Taha\r\n" +
                "20 Omri Ben Harush\r\n" +
                "21 Beram Kayal\r\n" +
                "18 Ofir Marciano\r\n" +
                "23 Yoav Jarafi\r\n" +
                "3 Dan Glazer\r\n" +
                "5 Ayad Habashi\r\n" +
                "8 Almog Cohen\r\n" +
                "9 Eylon Almog\r\n" +
                "10 Dia Seba\r\n" +
                "13 Taleb Tawatha\r\n" +
                "14 Ben Sahar\r\n" +
                "16 Yonatan Cohen\r\n" +
                "19 Orel Dgani\r\n" +
                "22 Hatem Elhamed"
        var team2Actual: String? = null
        val latch = CountDownLatch(2)
        var isFirstChange = true
        val testOwner = TestLifecycleOwner()
        testOwner.handleEvent(Lifecycle.Event.ON_CREATE)
        testOwner.handleEvent(Lifecycle.Event.ON_START)
        testOwner.handleEvent(Lifecycle.Event.ON_RESUME)


        val stream = Thread.currentThread().contextClassLoader.getResourceAsStream("chelsea_bournemouth.jpg")
        val bufferedInputStream = BufferedInputStream(stream)
        val bitmapTmp = BitmapFactory.decodeStream(bufferedInputStream);
        val exif = ExifInterface(stream)
        val bitmap = bitmapFromBitmap(bitmapTmp, exif)

        val app = ApplicationProvider.getApplicationContext<Application>()

        val viewModel = TeamsViewModel(app, handle = SavedStateHandle())
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.setInputImag(bitmap)
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.rawTeam2.observe(testOwner, Observer { value ->
                if (isFirstChange) {
                    isFirstChange = false
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(100)
                        viewModel.acceptResult()
                    }
                }
            })
            viewModel.team1.observe(testOwner, Observer { value ->
                team1Actual = value
                latch.countDown()
            })
            viewModel.team2.observe(testOwner, Observer { value ->
                team2Actual = value
                latch.countDown()
            })
        }
        latch.await()

        //Long strings sometimes are displayed incorrectly in test summary
        val team1ListActual = team1Actual?.split("\r\n")
        val team1ListExpected = team1Expected.split("\r\n")
        val team2ListActual = team2Actual?.split("\r\n")
        val team2ListExpected = team2Expected.split("\r\n")

        assertEquals(team1ListExpected, team1ListActual)
        assertEquals(team2ListExpected, team2ListActual)

    }*/
}


    fun bitmapFromBitmap(bitmap: Bitmap, exif: ExifInterface): Bitmap {
        val MAX_HEIGHT = 4096
        val MAX_WIDTH = 4096
        var rotation=0
        if (bitmap.width>bitmap.height) {
            rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_ROTATE_270)
            rotation += 90
        }
        val rotationInDegrees = rotation.toFloat()
        val mutableImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        bitmap.recycle()

        val matrix = Matrix()
        var height = mutableImage.height
        var width = mutableImage.width
        val rational = Rational(height, width)// 4:3
        if (height > MAX_HEIGHT)//TODO Better scaling for big files
            height = MAX_HEIGHT
        if (width > MAX_WIDTH)
            width = MAX_WIDTH

        if (rotation != 0) {
            matrix.preRotate(rotationInDegrees)
            return Bitmap.createBitmap(mutableImage, 0, 0, width, height, matrix, true)
        } else if (bitmap.height > MAX_HEIGHT || bitmap.width > MAX_WIDTH) {
            return Bitmap.createBitmap(mutableImage, 0, 0, width, height)
        }
        return mutableImage
    }
