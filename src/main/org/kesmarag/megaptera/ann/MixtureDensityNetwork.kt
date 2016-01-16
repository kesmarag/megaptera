package org.kesmarag.megaptera.ann

import org.kesmarag.megaptera.linear.ColumnVector
import org.kesmarag.megaptera.linear.DenseMatrix
import org.kesmarag.megaptera.linear.RowVector
import org.kesmarag.megaptera.utils.sigmoid

class MixtureDensityNetwork {
    public val inputs: Int
    public val hidden: Int
    public val outputs: Int
    public val mixtures: Int
    public var W1: DenseMatrix
        private set
    public var W2: DenseMatrix
        private set

    constructor(_inputs: Int, _hidden: Int, _outputs: Int, _mixtures: Int) {
        inputs = _inputs
        hidden = _hidden
        outputs = _outputs
        mixtures = _mixtures
        W1 = DenseMatrix(inputs, hidden)
        W1.randomize(0.0,1.0)
        W2 = DenseMatrix(hidden, (outputs*outputs+3*outputs+2)*mixtures/2)
        W2.randomize(0.0,1.0)
    }


    public fun apply(inputVector: RowVector): RowVector {
        val a1 = inputVector*W1
        val z1 = sigmoid(a1)
        val a2 = z1*W2
        println(a2)
        println("output layer = ${W2.columns}")
        return a2
    }

}