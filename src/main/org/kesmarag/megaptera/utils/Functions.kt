package org.kesmarag.megaptera.utils

import org.kesmarag.megaptera.linear.ColumnVector
import org.kesmarag.megaptera.linear.RowVector

fun sigmoid(a: Double): Double = 1.0 / (1.0 + Math.exp(-a))

fun tanhBased(a: Double): Double = 1.7159 * Math.tanh(2.0 * a / 3.0)

fun sigmoid(vector: ColumnVector): ColumnVector {
    val sigmoidVector = ColumnVector(vector.dimension)
    for (i in 0..vector.dimension - 1) {
        sigmoidVector[i] = sigmoid(vector[i])
    }
    return sigmoidVector
}

fun tanhBased(vector: ColumnVector): ColumnVector {
    val tanhBasedVector = ColumnVector(vector.dimension)
    for (i in 0..vector.dimension - 1) {
        tanhBasedVector[i] = tanhBased(vector[i])
    }
    return tanhBasedVector
}

fun tanhBased(vector: RowVector): RowVector {
    return tanhBased(vector.t()).t()
}

fun sigmoid(vector: RowVector): RowVector {
    return sigmoid(vector.t()).t()
}


fun softmax(vector: ColumnVector): ColumnVector {
    var softmaxVector = ColumnVector(vector.dimension)
    var sumOfExp = 0.0
    for (i in 0..vector.dimension - 1) {
        val exp = Math.exp(vector[i])
        sumOfExp += exp
        softmaxVector[i] = exp
    }
    softmaxVector = softmaxVector * (1.0 / sumOfExp)
    return softmaxVector
}

fun softmax(vector: RowVector): RowVector {
    return softmax(vector.t()).t()
}

fun exp(a: Double, vector: RowVector): RowVector {
    val expVector = RowVector(vector.dimension)
    for (i in 0..vector.dimension - 1) {
        expVector[i] = Math.exp(vector[i] * a)
    }

    return expVector
}