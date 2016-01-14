package org.kesmarag.megaptera.utils

import org.kesmarag.megaptera.linear.ColumnVector
import org.kesmarag.megaptera.linear.RowVector
import org.kesmarag.megaptera.linear.Vector
import org.kesmarag.megaptera.linear.VectorType

fun sigmoid(a: Double): Double = 1.0 / (1.0 + Math.exp(-a))

fun sigmoid(vector: ColumnVector): ColumnVector {
    val sigmoidVector = ColumnVector(vector.dimension)
    for (i in 0..vector.dimension - 1) {
        sigmoidVector[i] = sigmoid(vector[i])
    }
    return sigmoidVector
}

fun softmax(vector: ColumnVector): ColumnVector {
    var softmaxVector = ColumnVector(vector.dimension)
    var sumOfExp = 0.0
    for (i in 0..vector.dimension-1){
        val exp = Math.exp(vector[i])
        sumOfExp+= exp
        softmaxVector[i] = exp
    }
    softmaxVector = softmaxVector*(1.0/sumOfExp)
    return softmaxVector
}
