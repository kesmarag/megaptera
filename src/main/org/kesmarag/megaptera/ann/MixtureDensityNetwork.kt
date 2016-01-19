package org.kesmarag.megaptera.ann

import org.kesmarag.megaptera.linear.ColumnVector
import org.kesmarag.megaptera.linear.DenseMatrix
import org.kesmarag.megaptera.linear.UpperTriangularMatrix
import org.kesmarag.megaptera.utils.sigmoid
import org.kesmarag.megaptera.utils.softmax

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
        W1 = DenseMatrix(hidden, inputs)
        W1.randomize(-1.0, 1.0)
        W2 = DenseMatrix((outputs * outputs + 3 * outputs + 2) * mixtures / 2, hidden)
        W2.randomize(-1.0, 1.0)
    }

    public fun getMixtureDensity(): MixtureDensity? {
        return null
    }

    public fun apply(inputVector: ColumnVector): ColumnVector {
        val a1 = W1 * inputVector
        val z1 = sigmoid(a1)
        val a2 = W2 * z1
        var weights = a2[0..mixtures - 1]
        var pointer = mixtures
        weights = softmax(weights)
        var means = DenseMatrix(mixtures, outputs)
        for (i in 0..mixtures - 1) {
            for (j in 0..outputs - 1) {
                means[i, j] = a2[i * outputs + j + pointer]
            }
        }
        pointer += mixtures * outputs


        var alphas = Array(mixtures) { UpperTriangularMatrix(outputs) }
        var alphasVector = a2[pointer..a2.dimension - 1]
        val perMixture = alphasVector.dimension / mixtures
        MixtureDensity(mixtures,outputs,a2)
        return a2
    }

    operator fun invoke(inputVector: ColumnVector): ColumnVector {
        return this.apply(inputVector)
    }

}