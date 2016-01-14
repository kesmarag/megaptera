package org.kesmarag.megaptera.utils

import org.kesmarag.megaptera.linear.ColumnVector
import org.kesmarag.megaptera.linear.RowVector
import org.kesmarag.megaptera.linear.Vector
import org.kesmarag.megaptera.linear.VectorType


fun DoubleArray.toColumnVector(): Vector {
    val tmpVector = ColumnVector(this.size)
    for (i in 0..this.size - 1) {
        tmpVector[i] = this[i]
    }
    return tmpVector
}

fun DoubleArray.toRowVector(): Vector {
    val tmpVector = RowVector(this.size)
    for (i in 0..this.size - 1) {
        tmpVector[i] = this[i]
    }
    return tmpVector
}

fun distance(vector1: Vector, vector2: Vector): Double = (vector1 - vector2).norm2()

infix operator fun Double.times(vector: Vector): Vector = vector.times(this)
infix operator fun Int.times(vector: Vector): Vector = vector.times(this.toDouble())

fun Math.exp(a: Double,vector: Vector): Vector{
    val expaVector: Vector
    if (vector.type == VectorType.COLUMN_VECTOR){
        expaVector = ColumnVector(vector.dimension)
    }else{
        expaVector = RowVector(vector.dimension)
    }
    for (i in 0..vector.dimension-1){
        expaVector[i] = Math.exp(a*vector[i])
    }
    return expaVector
}