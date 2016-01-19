package org.kesmarag.megaptera.ann

import org.kesmarag.megaptera.linear.ColumnVector
import org.kesmarag.megaptera.utils.softmax


class MixtureDensity {
    val mixtures: Int
    val dimension: Int
    public var weights: ColumnVector
    //public val means: Array<ColumnVector>
    //public val alphas: Array<UpperTriangularMatrix>
    constructor(_mixtures: Int, _dimension: Int, output: ColumnVector) {
        mixtures = _mixtures
        dimension = _dimension
        if ((_dimension*_dimension+3*_dimension+2)*mixtures/2 != output.dimension){
            throw IllegalStateException("wrong dimension")
        }
        //weights = ColumnVector(_mixtures)
        weights = output[0.._mixtures-1]
        weights = softmax(weights)
        println(weights)
    }

    public fun toFile() {

    }


}