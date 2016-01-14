package org.kesmarag.megaptera.linear

class ColumnVector(_dimension: Int) : Vector(_dimension) {
    override fun t(): RowVector {
        val tmpVector = RowVector(dimension)
        for (i in 0..dimension - 1) {
            tmpVector[i] = this[i]
        }
        return tmpVector
    }

    override val type = VectorType.COLUMN_VECTOR

}

