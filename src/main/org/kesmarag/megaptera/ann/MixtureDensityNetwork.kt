package org.kesmarag.megaptera.ann

import org.kesmarag.megaptera.data.Observation
import org.kesmarag.megaptera.data.ObservationSet
import org.kesmarag.megaptera.linear.ColumnVector
import org.kesmarag.megaptera.linear.DenseMatrix
import org.kesmarag.megaptera.utils.*
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
        val limit1 = 1.0*Math.sqrt(6.0/(hidden+inputs).toDouble())
        W1.randomize(-limit1, limit1)
        val dim = (outputs * outputs + 3 * outputs + 2) * mixtures / 2
        W2 = DenseMatrix(dim, hidden)
        val limit2 = 0.5*Math.sqrt(6.0/(hidden+dim).toDouble())
        W2.randomizeNeuralNetwork(-limit2, limit2,mixtures,outputs)
       // W2.randomize(-limit2, limit2)
        println(W2)
    }

    public fun getMixtureDensity(): MixtureDensity? {
        return null
    }

    public fun apply(inputVector: ColumnVector): ColumnVector {
        val a1 = W1 * inputVector
        //val z1 = sigmoid(a1)
        val z1 = tanhBased(a1)
        val a2 = W2 * z1
        return a2
    }



    public fun error(inputVector: ColumnVector, targetVector: ColumnVector): Double{
        val a2 = apply(inputVector)
        val md = MixtureDensity(mixtures, outputs)
        md.hyperParameters(a2)
        return -md[targetVector]
    }

    public fun trainingBatchSGD(data: MutableList<Observation>,
                                targets: MutableList<Observation>,
                                batchSize: Int,
                                steps: Int,
                                lambda: Double,
                                learning: Int,
                                l2parameter: Double ,
                                offset: Int ){
        var lambdaLocal = lambda
        var bool = true
        val dataSize = data.size
        val length = data[0].length
        //val epsilon = 0.001 // checking the derivatives using finite differences
        val l2 = l2parameter
        var decreasingFactor = 1.0
        var W1Best = DenseMatrix(hidden, inputs)
        val dim = (outputs * outputs + 3 * outputs + 2) * mixtures / 2
        var W2Best = DenseMatrix(dim, hidden)
        var best = 100.0
        var error = 100.0
        for (s in 1..steps){
            var dEdW1 = DenseMatrix(hidden, inputs)
            var dEdW2 = DenseMatrix((outputs * outputs + 3 * outputs + 2) * mixtures / 2, hidden)
            println("step = $s")
            for (b in 1..batchSize) {
                //println("  -> batch = $b")
                var chosen = Random().nextInt(dataSize-2)
                //var chosen = Random().nextInt(40)+980
                //println("->chosen = $chosen")
                var input = data[chosen].data.toColumnVector()
                var output = targets[chosen].data.toColumnVector()
                //input = softmax(input)
                //println(input)
                val a1 = W1 * input
                //val z1 = sigmoid(a1)
                val z1 = tanhBased(a1)
                val a2 = W2 * z1
                val md = MixtureDensity(mixtures, outputs)
                md.hyperParameters(a2)
                //println("det(Sigma) = ${md.detSigma()}")
                val d2 = md.derivative(output)
                val d1 = ColumnVector(hidden)
                val tmp = W2.t() * d2
                for (i in 0..hidden - 1) {

                    //d1[i] = tmp[i] * sigmoid(a1[i]) * (1 - sigmoid(a1[i]))
                    // derivative of the activation function tanhBased
                    d1[i] = tmp[i] * (1.7159-tanhBased(a1[i])*tanhBased(a1[i])/1.7159)*2.0/3.0
                }
                for (i in 0..hidden - 1) {
                    for (j in 0..inputs - 1) {
                        dEdW1[i, j] =  dEdW1[i, j] + d1[i] * input[j] + l2*2.0*W1[i,j]
                        /*
                        W1[i,j] = W1[i,j] + epsilon
                        val errorPlus = this.error(input,output)
                        W1[i,j] = W1[i,j] - 2*epsilon
                        val errorMinus = this.error(input,output)
                        val derivativeEstimator = (errorPlus-errorMinus)/(2*epsilon)
                        println("backprop = ${dEdW1[i,j]} , estimator = $derivativeEstimator")
                        W1[i,j] = W1[i,j] + epsilon
                        */
                    }
                }
                for (i in 0..(outputs * outputs + 3 * outputs + 2) * mixtures / 2 - 1) {
                    for (j in 0..hidden - 1) {
                        dEdW2[i, j] = dEdW2[i, j] + d2[i] * z1[j] + l2*2.0*W2[i,j]
                        /*
                        W2[i,j] = W2[i,j] + epsilon
                        val errorPlus = this.error(input,output)
                        W2[i,j] = W2[i,j] - 2*epsilon
                        val errorMinus = this.error(input,output)
                        val derivativeEstimator = (errorPlus-errorMinus)/(2*epsilon)
                        //println("(i=$i,j=$j) backprop = ${dEdW2[i,j]} , estimator = $derivativeEstimator")
                        W2[i,j] = W2[i,j] + epsilon
                        */
                    }
                }

            }
            if (s>offset){
                decreasingFactor = learning.toDouble()/(learning.toDouble()+(s-offset).toDouble())
            }
            println("factor = $decreasingFactor")
            //W1 = W1 - dEdW1 *(lambdaLocal/batchSize.toDouble())*decreasingFactor
            W1.minusAssign(dEdW1 *(lambdaLocal/batchSize.toDouble())*decreasingFactor)
            //W2 = W2 - dEdW2 * (lambdaLocal/batchSize.toDouble())*decreasingFactor
            W2.minusAssign(dEdW2 * (lambdaLocal/batchSize.toDouble())*decreasingFactor)
            //println((lambda/batchSize.toDouble())*(learning.toDouble()/(learning.toDouble()+s.toDouble())))
            error = this.error(data[400].data.toColumnVector(),targets[400].data.toColumnVector())
            if (error < best){
                W1Best = W1.clone()
                W2Best = W2.clone()
                best = error
            }
            println(error)
            //println(W2)
        }
        W1 = W1Best.clone()
        W2 = W2Best.clone()
        println("best Error = $best")
    }

    public fun trainingBatchSGD(odata: ObservationSet,
                                otargets: ObservationSet,
                                batchSize: Int,
                                steps: Int,
                                lambda: Double,
                                learning: Int,
                                l2parameter: Double ,
                                offset: Int){
        trainingBatchSGD(odata.data,otargets.data,batchSize,steps,lambda,learning,l2parameter,offset)

    }

    operator fun invoke(inputVector: ColumnVector): ColumnVector {
        return this.apply(inputVector)
    }

}