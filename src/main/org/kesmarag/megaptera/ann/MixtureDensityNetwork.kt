package org.kesmarag.megaptera.ann

import org.kesmarag.megaptera.linear.ColumnVector
import org.kesmarag.megaptera.linear.DenseMatrix

class MixtureDensityNetwork {
    val inputs: Int
    val hidden: Int
    val outputs: Int
    val mixtures: Int
    val W1 : DenseMatrix
    val W2 : DenseMatrix


    constructor(_inputs: Int, _hidden: Int, _outputs: Int, _mixtures: Int){
        inputs = _inputs
        hidden = _hidden
        outputs = _outputs
        mixtures = _mixtures
        W1 = DenseMatrix(inputs,hidden)
        W2 = DenseMatrix(hidden,outputs)
    }

    public fun forward(inputVector: ColumnVector){

    }

}