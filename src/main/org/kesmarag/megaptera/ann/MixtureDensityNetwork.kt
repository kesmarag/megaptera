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
        val md = MixtureDensity(mixtures, outputs)
        md.hyperParameters(a2)
        return a2
    }


    public fun derivativeOut(inputVector: ColumnVector, outputVector: ColumnVector) {

    }

    public fun adaptOne(inputVector: ColumnVector, outputVector: ColumnVector, lambda: Double) {
        val a1 = W1 * inputVector
        val z1 = sigmoid(a1)
        val a2 = W2 * z1
        val md = MixtureDensity(mixtures, outputs)
        md.hyperParameters(a2)
        val d2 = md.derivative(outputVector)
        val d1 = ColumnVector(hidden)
        val tmp = W2.t() * d2
        for (i in 0..hidden - 1) {
            d1[i] = tmp[i] * sigmoid(a1[i]) * (1 - sigmoid(a1[i]))
        }
        var dEdW1 = DenseMatrix(hidden, inputs)
        for (i in 0..hidden-1){
            for (j in 0..inputs-1){
                dEdW1[i,j]= d1[i]*inputVector[j]
            }
        }
        //println(dEdW1)
        var dEdW2 = DenseMatrix((outputs * outputs + 3 * outputs + 2) * mixtures / 2, hidden)
        for (i in 0..(outputs * outputs + 3 * outputs + 2) * mixtures / 2 -1){
            for (j in 0..hidden-1){
                dEdW2[i,j]= d2[i]*z1[j]
            }
        }
       // println(dEdW2)
        W1 = W1 - dEdW1*lambda
        W2 = W2 - dEdW2*lambda

    }


    operator fun invoke(inputVector: ColumnVector): ColumnVector {
        return this.apply(inputVector)
    }

}