package org.kesmarag.megaptera.ann

import org.kesmarag.megaptera.linear.DenseMatrix
import org.kesmarag.megaptera.linear.RowVector
import org.kesmarag.megaptera.linear.UpperTriangularMatrix


class MixtureDensity {
    public val weights: RowVector
    public val means: DenseMatrix
    public val alphas: Array<UpperTriangularMatrix>
    constructor(_weight: RowVector, _means: DenseMatrix, _alphas: Array<UpperTriangularMatrix>){
        weights = _weight
        means = _means
        alphas = _alphas
    }

    public fun toFile(){

    }



}