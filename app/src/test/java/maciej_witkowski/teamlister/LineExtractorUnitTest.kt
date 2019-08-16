package maciej_witkowski.teamlister

import android.graphics.Rect
import com.google.firebase.ml.vision.text.FirebaseVisionText
import maciej_witkowski.teamlister.model.LineExtractor
import maciej_witkowski.teamlister.model.PlayerData
import maciej_witkowski.teamlister.model.TextLineLight
import maciej_witkowski.teamlister.utils.TextUtils
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.mockito.MockitoAnnotations
import org.junit.Before


@RunWith(RobolectricTestRunner::class)
class LineExtractorUnitTest {

    @Mock
    val line1: FirebaseVisionText.Line? = null
    @Mock
    val line2: FirebaseVisionText.Line? = null
    @Mock
    val line3: FirebaseVisionText.Line? = null
    @Mock
    val line4: FirebaseVisionText.Line? = null
    @Mock
    val line5: FirebaseVisionText.Line? = null
    @Mock
    val line6: FirebaseVisionText.Line? = null
    @Mock
    val line7: FirebaseVisionText.Line? = null
    @Mock
    val line8: FirebaseVisionText.Line? = null
    @Mock
    val line9: FirebaseVisionText.Line? = null
    @Mock
    val line10: FirebaseVisionText.Line? = null
    @Mock
    val line11: FirebaseVisionText.Line? = null
    @Mock
    val line12: FirebaseVisionText.Line? = null
    @Mock
    val line13: FirebaseVisionText.Line? = null
    @Mock
    val line14: FirebaseVisionText.Line? = null
    @Mock
    val line15: FirebaseVisionText.Line? = null
    @Mock
    val line16: FirebaseVisionText.Line? = null
    @Mock
    val line17: FirebaseVisionText.Line? = null
    @Mock
    val line18: FirebaseVisionText.Line? = null
    @Mock
    val line19: FirebaseVisionText.Line? = null
    @Mock
    val line20: FirebaseVisionText.Line? = null
    @Mock
    val line21: FirebaseVisionText.Line? = null
    @Mock
    val line22: FirebaseVisionText.Line? = null
    @Mock
    val line23: FirebaseVisionText.Line? = null
    @Mock
    val line24: FirebaseVisionText.Line? = null

    /*@Mock
    private val someClassA: SomeClass? = null
    @Mock
    private val someClassB: SomeClass? = null*/


    @Before
    fun initMocks() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun mockingTest() {
        val expectedText = "someText"
        val expectedRect = Rect(100, 100, 200, 200)
        val textLine = mock(FirebaseVisionText.Line::class.java)
        `when`(textLine.text)
            .thenReturn("someText")
        `when`(textLine.boundingBox)
            .thenReturn(Rect(100, 100, 200, 200))

        assertEquals(expectedText, textLine.text)
        assertEquals(expectedRect, textLine.boundingBox)
    }

    @Test
    fun leftNormalOrder() {//test_5space.jpg 12 right, 6 wrong (1,2,3,4,11,14)
        val textLinesExpected = mutableListOf(
            TextLineLight(PlayerData("5", "Fifth NAME"), Rect(129, 265, 375, 295)),
            TextLineLight(PlayerData("6", "Sixth NAME"), Rect(129, 299, 395, 344)),
            TextLineLight(PlayerData("7", "Seventh NAME"), Rect(130, 350, 424, 376)),
            TextLineLight(PlayerData("8", "Eighth NAME"), Rect(130, 388, 403, 427)),
            TextLineLight(PlayerData("9", "Ninth NAME"), Rect(129, 432, 389, 462)),
            TextLineLight(PlayerData("10", "Tenth NAME"), Rect(132, 476, 404, 502)),
            TextLineLight(PlayerData("12", "Twelfth NAME"), Rect(132, 560, 436, 586)),
            TextLineLight(PlayerData("13", "Thirteenth NAME"), Rect(133, 600, 474, 626)),
            TextLineLight(PlayerData("15", "Fifteenth NAME"), Rect(132, 680, 453, 714)),
            TextLineLight(PlayerData("16", "Sixteenth NAME"), Rect(132, 723, 460, 754)),
            TextLineLight(PlayerData("17", "Seventeenth NAME"), Rect(132, 768, 500, 796)),
            TextLineLight(PlayerData("18", "Eighteenth NAME"), Rect(130, 800, 482, 850)),
            TextLineLight(PlayerData("1", "First NAME"), Rect(130, 101, 372, 126)),
            TextLineLight(PlayerData("2", "Second NAME"), Rect(128, 142, 413, 167)),
            TextLineLight(PlayerData("3", "Third NAME"), Rect(129, 180, 385, 212)),
            TextLineLight(PlayerData("4", "Fourth NAME"), Rect(129, 224, 402, 254)),
            TextLineLight(PlayerData("11", "Eleventh NAME"), Rect(132, 518, 452, 543)),
            TextLineLight(PlayerData("14", "Fourteenth NAME"), Rect(132, 643, 481, 668))
        )

        val imageWidth = 976
        `when`(line1?.text).thenReturn("First NAME")
        `when`(line1?.boundingBox)
            .thenReturn(
                Rect(191, 101, 372, 126)
            )
        `when`(line2?.text)
            .thenReturn("Second NAME")
        `when`(line2?.boundingBox)
            .thenReturn(
                Rect(193, 142, 413, 167)
            )
        `when`(line3?.text)
            .thenReturn("Third NAME")
        `when`(line3?.boundingBox)
            .thenReturn(
                Rect(191, 180, 385, 212)
            )
        `when`(line4?.text)
            .thenReturn("Fourth NAME")
        `when`(line4?.boundingBox)
            .thenReturn(
                Rect(191, 224, 402, 254)
            )
        `when`(line5?.text)
            .thenReturn("1")
        `when`(line5?.boundingBox)
            .thenReturn(
                Rect(130, 103, 142, 125)
            )
        `when`(line6?.text)
            .thenReturn("2")
        `when`(line6?.boundingBox)
            .thenReturn(
                Rect(128, 145, 144, 166)
            )
        `when`(line7?.text)
            .thenReturn("3")
        `when`(line7?.boundingBox)
            .thenReturn(
                Rect(129, 185, 143, 208)
            )
        `when`(line8?.text)
            .thenReturn("4")
        `when`(line8?.boundingBox)
            .thenReturn(
                Rect(129, 227, 144, 250)
            )
        `when`(line9?.text)
            .thenReturn("5 Fifth NAME")
        `when`(line9?.boundingBox)
            .thenReturn(
                Rect(129, 265, 375, 295)
            )
        `when`(line10?.text)
            .thenReturn("6 Sixth NAME")
        `when`(line10?.boundingBox)
            .thenReturn(
                Rect(129, 299, 395, 344)
            )
        `when`(line11?.text)
            .thenReturn("7 Seventh NAME")
        `when`(line11?.boundingBox)
            .thenReturn(
                Rect(130, 350, 424, 376)
            )
        `when`(line12?.text)
            .thenReturn("8 Eighth NAME")
        `when`(line12?.boundingBox)
            .thenReturn(
                Rect(130, 388, 403, 427)
            )
        `when`(line13?.text)
            .thenReturn("9 Ninth NAME")
        `when`(line13?.boundingBox)
            .thenReturn(
                Rect(129, 432, 389, 462)
            )
        `when`(line14?.text)
            .thenReturn("10 Tenth NAME")
        `when`(line14?.boundingBox)
            .thenReturn(
                Rect(132, 476, 404, 502)
            )
        `when`(line15?.text)
            .thenReturn("11")
        `when`(line15?.boundingBox)
            .thenReturn(
                Rect(132, 519, 159, 543)
            )
        `when`(line16?.text)
            .thenReturn("12 Twelfth NAME")
        `when`(line16?.boundingBox)
            .thenReturn(
                Rect(132, 560, 436, 586)
            )
        `when`(line17?.text)
            .thenReturn("13 Thirteenth NAME")
        `when`(line17?.boundingBox)
            .thenReturn(Rect(133, 600, 474, 626))
        `when`(line18?.text)
            .thenReturn("14")
        `when`(line18?.boundingBox)
            .thenReturn(Rect(132, 643, 162, 668))
        `when`(line19?.text)
            .thenReturn("15 Fifteenth NAME")
        `when`(line19?.boundingBox)
            .thenReturn(Rect(132, 680, 453, 714))
        `when`(line20?.text)
            .thenReturn("16 Sixteenth NAME")
        `when`(line20?.boundingBox)
            .thenReturn(Rect(132, 723, 460, 754))
        `when`(line21?.text)
            .thenReturn("17 Seventeenth NAME")
        `when`(line21?.boundingBox)
            .thenReturn(Rect(132, 768, 500, 796))
        `when`(line22?.text)
            .thenReturn("18 Eighteenth NAME")
        `when`(line22?.boundingBox)
            .thenReturn(
                Rect(130, 800, 482, 850)
            )
        `when`(line23?.text)
            .thenReturn(
                "Eleventh NAME"
            )
        `when`(line23?.boundingBox)
            .thenReturn(
                Rect(209, 518, 452, 543)
            )
        `when`(line24?.text)
            .thenReturn(
                "Fourteenth NAME"
            )
        `when`(line24?.boundingBox)
            .thenReturn(
                Rect(210, 643, 481, 668)
            )


        val lineList = listOf(
            line1, line2, line3, line4, line5, line6, line7, line8, line9, line10, line11, line12,
            line13, line14, line15, line16, line17, line18, line19, line20, line21, line22, line23, line24
        )
        val textBlock = mock(FirebaseVisionText.TextBlock::class.java)
        `when`(textBlock.lines)
            .thenReturn(lineList)
        val blockList = listOf<FirebaseVisionText.TextBlock>(
            textBlock
        )
        val firebaseVisionText = mock(FirebaseVisionText::class.java)
        `when`(firebaseVisionText.textBlocks)
            .thenReturn(blockList)
        val textLinesActual = LineExtractor().getValidTextLines(firebaseVisionText, imageWidth)


        assertEquals(textLinesExpected, textLinesActual)
    }

    @Test
    fun leftReversedOrder() {//left_only_inverted.jpg 11 right, 5 wrong (4,6,7,9,14)
        val textLinesExpected = mutableListOf(
            TextLineLight(PlayerData("1", "First NAME"), Rect(228, 103, 463, 128)),
            TextLineLight(PlayerData("2", "Second NAME"), Rect(194, 145, 463, 171)),
            TextLineLight(PlayerData("3", "Third NAME"), Rect(219, 186, 466, 214)),
            TextLineLight(PlayerData("5", "Fifth NAME"), Rect(228, 270, 466, 297)),
            TextLineLight(PlayerData("8", "Eighth NAME"), Rect(200, 389, 465, 429)),
            TextLineLight(PlayerData("10", "Tenth NAME"), Rect(219, 476, 485, 505)),
            TextLineLight(PlayerData("11", "Eleventh NAME"), Rect(174, 520, 480, 545)),
            TextLineLight(PlayerData("12", "Twelfth NAME"), Rect(192, 562, 484, 588)),
            TextLineLight(PlayerData("13", "Thirteenth NAME"), Rect(155, 604, 483, 629)),
            TextLineLight(PlayerData("15", "Fifteenth NAME"), Rect(173, 687, 483, 712)),
            TextLineLight(PlayerData("16", "Sixteenth NAME"), Rect(166, 726, 484, 757)),
            TextLineLight(PlayerData("17", "Seventeenth NAME"), Rect(130, 771, 485, 796)),
            TextLineLight(PlayerData("18", "Eighteenth NAME"), Rect(148, 812, 484, 846)),
            TextLineLight(PlayerData("4", "Fourth NAME"), Rect(200, 228, 466, 253)),
            TextLineLight(PlayerData("6", "Sixth NAME"), Rect(210, 312, 465, 338)),
            TextLineLight(PlayerData("7", "Sixteenth NAME"), Rect(184, 350, 467, 381)),
            TextLineLight(PlayerData("9", "Seventeenth NAME"), Rect(218, 437, 469, 462)),
            TextLineLight(PlayerData("14", "Eighteenth NAME"), Rect(146, 645, 484, 670))
        )
        val imageWidth = 968
        `when`(line1?.text).thenReturn("First NAME 1")
        `when`(line1?.boundingBox)
            .thenReturn( Rect(228, 103,463, 128))
        `when`(line2?.text).thenReturn("Second NAME 2")
        `when`(line2?.boundingBox)
            .thenReturn( Rect(194, 145,463, 171))
        `when`(line3?.text).thenReturn("Third NAME 3")
        `when`(line3?.boundingBox)
            .thenReturn( Rect(219, 186,466, 214))
        `when`(line4?.text).thenReturn("Fourth NAME")
        `when`(line4?.boundingBox)
            .thenReturn( Rect(200, 228,412, 253))
        `when`(line5?.text).thenReturn("4")
        `when`(line5?.boundingBox)
            .thenReturn( Rect(449, 229,466, 252))
        `when`(line6?.text).thenReturn("Fifth NAME 5")
        `when`(line6?.boundingBox)
            .thenReturn( Rect(228, 270,466, 297))
        `when`(line7?.text).thenReturn("6")
        `when`(line7?.boundingBox)
            .thenReturn( Rect(450, 317,465, 336))
        `when`(line8?.text).thenReturn("7")
        `when`(line8?.boundingBox)
            .thenReturn( Rect(451, 353,467, 377))
        `when`(line9?.text).thenReturn("Sixth NAME")
        `when`(line9?.boundingBox)
            .thenReturn( Rect(210, 312,410, 338))
        `when`(line10?.text).thenReturn("Seventh NAME")
        `when`(line10?.boundingBox)
            .thenReturn( Rect(184, 350,413, 381))
        `when`(line11?.text).thenReturn("Eighth NAME 8")
        `when`(line11?.boundingBox)
            .thenReturn( Rect(200, 389,465, 429))
        `when`(line12?.text).thenReturn("Ninth NAME")
        `when`(line12?.boundingBox)
            .thenReturn( Rect(218, 437,416, 462))
        `when`(line13?.text).thenReturn("9")
        `when`(line13?.boundingBox)
            .thenReturn( Rect(454, 437,469, 461))
        `when`(line14?.text).thenReturn("Tenth NAME 10")
        `when`(line14?.boundingBox)
            .thenReturn( Rect(219, 476,485, 505))
        `when`(line15?.text).thenReturn("Eleventh NAME 11")
        `when`(line15?.boundingBox)
            .thenReturn( Rect(174, 520,480, 545))
        `when`(line16?.text).thenReturn("Twelfth NAME 12")
        `when`(line16?.boundingBox)
            .thenReturn( Rect(192, 562,484, 588))
        `when`(line17?.text).thenReturn("Thirteenth NAME 13")
        `when`(line17?.boundingBox)
            .thenReturn( Rect(155, 604,483, 629))
        `when`(line18?.text).thenReturn("Fourteenth NAME")
        `when`(line18?.boundingBox)
            .thenReturn( Rect(146, 645,418, 670))
        `when`(line19?.text).thenReturn("14")
        `when`(line19?.boundingBox)
            .thenReturn( Rect(452, 646,484, 669))
        `when`(line20?.text).thenReturn("Fifteenth NAME 15")
        `when`(line20?.boundingBox)
            .thenReturn( Rect(173, 687,483, 712))
        `when`(line21?.text).thenReturn("Sixteenth NAME 16")
        `when`(line21?.boundingBox)
            .thenReturn( Rect(166, 726,484, 757))
        `when`(line22?.text).thenReturn("Seventeenth NAME 17")
        `when`(line22?.boundingBox)
            .thenReturn( Rect(130, 771,485, 796))
        `when`(line23?.text).thenReturn("Eighteenth NAME 18")
        `when`(line23?.boundingBox)
            .thenReturn( Rect(148, 812,484, 846))



        val lineList = listOf(
            line1, line2, line3, line4, line5, line6, line7, line8, line9, line10, line11, line12,
            line13, line14, line15, line16, line17, line18, line19, line20, line21, line22, line23
        )
        val textBlock = mock(FirebaseVisionText.TextBlock::class.java)
        `when`(textBlock.lines)
            .thenReturn(lineList)
        val blockList = listOf<FirebaseVisionText.TextBlock>(
            textBlock
        )
        val firebaseVisionText = mock(FirebaseVisionText::class.java)
        `when`(firebaseVisionText.textBlocks)
            .thenReturn(blockList)
        val textLinesActual = LineExtractor().getValidTextLines(firebaseVisionText, imageWidth)


        assertEquals(textLinesExpected, textLinesActual)







    }
}