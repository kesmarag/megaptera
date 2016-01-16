package org.kesmarag.megaptera.utils

import org.kesmarag.megaptera.linear.ColumnVector
import org.kesmarag.megaptera.linear.RowVector
import org.kesmarag.megaptera.linear.Vector
import org.kesmarag.megaptera.linear.VectorType


fun DoubleArray.toColumnVector(): ColumnVector {
    val tmpVector = ColumnVector(this.size)
    for (i in 0..this.size - 1) {
        tmpVector[i] = this[i]
    }
    return tmpVector
}

fun DoubleArray.toRowVector(): RowVector {
    val tmpVector = RowVector(this.size)
    for (i in 0..this.size - 1) {
        tmpVector[i] = this[i]
    }
    return tmpVector
}

fun distance(vector1: ColumnVector, vector2: ColumnVector): Double = (vector1 - vector2).norm2()
fun distance(vector1: RowVector, vector2: RowVector): Double = (vector1 - vector2).norm2()

infix operator fun Double.times(vector: ColumnVector): ColumnVector = vector.times(this)
infix operator fun Int.times(vector: ColumnVector): ColumnVector = vector.times(this.toDouble())
infix operator fun Double.times(vector: RowVector): RowVector = vector.times(this)
infix operator fun Int.times(vector: RowVector): RowVector = vector.times(this.toDouble())


fun Math.exp(a: Double,vector: ColumnVector): ColumnVector{
    val expaVector = ColumnVector(vector.dimension)
    for (i in 0..vector.dimension-1){
        expaVector[i] = Math.exp(a*vector[i])
    }
    return expaVector
}

fun Math.exp(a: Double, vector: RowVector) : RowVector{
    val expaVector = RowVector(vector.dimension)
    for (i in 0..vector.dimension-1){
        expaVector[i] = Math.exp(a*vector[i])
    }
    return expaVector
}