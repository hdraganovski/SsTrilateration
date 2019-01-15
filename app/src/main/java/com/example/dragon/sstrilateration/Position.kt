package com.example.dragon.sstrilateration

data class Position(
    var tag: String,
    var x: Double,
    var y: Double,
    var d: Double,
    var no: Int = 0
) {

    fun xyVectorized() = doubleArrayOf(x, y)

    companion object {
        const val TAG_WIFI = "wifi"
        const val TAG_BLUETOOTH = "blue"
        const val TAG_TEST_DATA = "test"
        const val TAG_GENERATED = "gen"
    }
}
