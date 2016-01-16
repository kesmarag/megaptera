package org.kesmarag.megaptera.ann

import org.kesmarag.megaptera.linear.DenseMatrix
import org.kesmarag.megaptera.linear.RowVector


class MixtureDensity {
    public val weights: RowVector
    public val means: DenseMatrix
    public val sigma: Array<DenseMatrix>
    constructor(_weight: RowVector, _means: DenseMatrix, _sigma: Array<DenseMatrix>){
        weights = _weight
        means = _means
        sigma = _sigma
    }

}