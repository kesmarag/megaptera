package org.kesmarag.megaptera.ann

import org.kesmarag.megaptera.linear.ColumnVector
import org.kesmarag.megaptera.linear.DenseMatrix
import org.kesmarag.megaptera.utils.Observation
import org.kesmarag.megaptera.utils.sigmoid
import org.kesmarag.megaptera.utils.tanhBased
import org.kesmarag.megaptera.utils.toColumnVector
import java.util.*

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
        val limit1 = 4.0*Math.sqrt(6.0/(hidden+inputs).toDouble())
        W1.randomize(-limit1, limit1)
        val dim = (outputs * outputs + 3 * outputs + 2) * mixtures / 2
        W2 = DenseMatrix(dim, hidden)
        val limit2 = 4.0*Math.sqrt(6.0/(hidden+dim).toDouble())
        W2.randomize(-limit2, limit2)
    }

    public fun getMixtureDensity(): MixtureDensity? {
        return null
    }

    public fun apply(inputVector: ColumnVector): ColumnVector {
        val a1 = W1 * inputVector
        val z1 = sigmoid(a1)
        //val z1 = tanhBased(a1)
        val a2 = W2 * z1
        return a2
    }

    public fun error(inputVector: ColumnVector, targetVector: ColumnVector): Double{
        val a2 = apply(inputVector)
        val md = MixtureDensity(mixtures, outputs)
        md.hyperParameters(a2)
        return -md[targetVector]
    }

    public fun derivativeCheck(){

    }




    public fun adaptOne(inputVector: ColumnVector, outputVector: ColumnVector, lambda: Double) {
        val epsilon = 0.000001
        val a1 = W1 * inputVector
        val z1 = sigmoid(a1)
        //val z1 = tanhBased(a1)
        val a2 = W2 * z1
        val md = MixtureDensity(mixtures, outputs)
        md.hyperParameters(a2)
        val d2 = md.derivative(outputVector)
        val d1 = ColumnVector(hidden)
        val tmp = W2.t() * d2
        for (i in 0..hidden - 1) {
            d1[i] = tmp[i] * sigmoid(a1[i]) * (1 - sigmoid(a1[i]))
            //d1[i] = tmp[i] * (1.7159-tanhBased(a1[i])*tanhBased(a1[i]))*2.0/3.0
        }
        var dEdW1 = DenseMatrix(hidden, inputs)
        for (i in 0..hidden - 1) {
            for (j in 0..inputs - 1) {
                dEdW1[i, j] = d1[i] * inputVector[j]
                W1[i,j] = W1[i,j] + epsilon
                val errorPlus = this.error(inputVector,outputVector)
                W1[i,j] = W1[i,j] - 2*epsilon
                val errorMinus = this.error(inputVector,outputVector)
                val derivativeEstimator = (errorPlus-errorMinus)/(2*epsilon)
                //println("backprop = ${dEdW1[i,j]} , estimator = $derivativeEstimator")
                W1[i,j] = W1[i,j] + epsilon
                //dEdW1[i, j] = derivativeEstimator
            }
        }
        //println(dEdW1)
        var dEdW2 = DenseMatrix((outputs * outputs + 3 * outputs + 2) * mixtures / 2, hidden)
        for (i in 0..(outputs * outputs + 3 * outputs + 2) * mixtures / 2 - 1) {
            for (j in 0..hidden - 1) {
                dEdW2[i, j] = d2[i] * z1[j]
                W2[i,j] = W2[i,j] + epsilon
                val errorPlus = this.error(inputVector,outputVector)
                W2[i,j] = W2[i,j] - 2*epsilon
                val errorMinus = this.error(inputVector,outputVector)
                val derivativeEstimator = (errorPlus-errorMinus)/(2*epsilon)
                //println("(i=$i,j=$j) backprop = ${dEdW2[i,j]} , estimator = $derivativeEstimator")
                W2[i,j] = W2[i,j] + epsilon
                //dEdW2[i, j] = derivativeEstimator
            }
        }
        W1 = W1 - dEdW1 * lambda
        W2 = W2 - dEdW2 * lambda
    }

    public fun trainingBatchSGD(data: MutableList<Observation>,
                                targets: MutableList<Observation>,
                                batchSize: Int,
                                steps: Int,
                                lambda: Double,
                                learning: Int){
        val dataSize = data.size
        val length = data[0].length
        val epsilon = 0.0001 // checking the derivatives using finite differences
        val l2 = 0.01
        for (s in 1..steps){
            var dEdW1 = DenseMatrix(hidden, inputs)
            var dEdW2 = DenseMatrix((outputs * outputs + 3 * outputs + 2) * mixtures / 2, hidden)
            println("step = $s")
            for (b in 1..batchSize) {
                //println("  -> batch = $b")
                var chosen = Random().nextInt(dataSize-1)
                //println("->chosen = $chosen")
                val input = data[chosen].data.toColumnVector()
                val output = targets[chosen].data.toColumnVector()
                val a1 = W1 * input
                val z1 = sigmoid(a1)
                val a2 = W2 * z1
                val md = MixtureDensity(mixtures, outputs)
                md.hyperParameters(a2)
                val d2 = md.derivative(output)
                val d1 = ColumnVector(hidden)
                val tmp = W2.t() * d2
                for (i in 0..hidden - 1) {
                    d1[i] = tmp[i] * sigmoid(a1[i]) * (1 - sigmoid(a1[i]))
                }
                for (i in 0..hidden - 1) {
                    for (j in 0..inputs - 1) {
                        dEdW1[i, j] =  dEdW1[i, j] + d1[i] * input[j] + l2*2.0+W1[i,j]
                    }
                }
                for (i in 0..(outputs * outputs + 3 * outputs + 2) * mixtures / 2 - 1) {
                    for (j in 0..hidden - 1) {
                        dEdW2[i, j] = dEdW2[i, j] + d2[i] * z1[j] + l2*2.0*W2[i,j]
                    }
                }

            }
            W1 = W1 - dEdW1 * (lambda/batchSize.toDouble())*(learning.toDouble()/(learning.toDouble()+s.toDouble()))
            W2 = W2 - dEdW2 * (lambda/batchSize.toDouble())*(learning.toDouble()/(learning.toDouble()+s.toDouble()))
            //println((lambda/batchSize.toDouble())*(learning.toDouble()/(learning.toDouble()+s.toDouble())))
            println(this.error(data[841].data.toColumnVector(),targets[841].data.toColumnVector()))
            //println(W2)
            //if (this.error(data[1681].data.toColumnVector(),targets[841].data.toColumnVector())<0) break
        }
    }


    operator fun invoke(inputVector: ColumnVector): ColumnVector {
        return this.apply(inputVector)
    }

}