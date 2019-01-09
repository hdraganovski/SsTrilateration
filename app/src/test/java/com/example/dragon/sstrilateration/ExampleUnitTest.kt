package com.example.dragon.sstrilateration

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


class ExampleUnitTest {

    @Test
    fun trilaterationTest() {
        val res = computePoint(TestData.TEST_POSITIONS)
        print("Result = $res")
    }
}
