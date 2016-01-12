package org.kesmarag.megaptera.linear

class RowVector(_dimension: Int) : Vector(_dimension) {
    override fun t() : ColumnVector{
        val tmpVector = ColumnVector(dimension)
        for (i in 0..dimension-1){
            tmpVector[i] = this[i]
        }
        return tmpVector
    }

    override val type = VectorType.ROW_VECTOR

    operator fun times(other: ColumnVector): Double{
        if (dimension != other.dimension ) {
            throw IllegalArgumentException("vectors with different dimensions")
        }
        var innerProduct = 0.0
        for (i in 0..dimension-1){
            innerProduct+=this[i]*other[i]
        }
        return innerProduct
    }
}

