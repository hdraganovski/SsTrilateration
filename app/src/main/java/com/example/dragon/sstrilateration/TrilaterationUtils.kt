package com.example.dragon.sstrilateration

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver
import com.lemmingapex.trilateration.TrilaterationFunction
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer

fun computePoint(points: List<Position>): Position {
    val p = points.map { it.xyVectorized() }.toTypedArray()
    val d = points.map { it.d }.toDoubleArray()

    val nllss = NonLinearLeastSquaresSolver(TrilaterationFunction(p, d), LevenbergMarquardtOptimizer())

    val optimum = nllss.solve()

    val result = optimum.point.toArray()

    return Position(
        tag = Position.TAG_GENERATED,
        x = result[0],
        y = result[1],
        d = 0.0,
        no = points.size
    )
}