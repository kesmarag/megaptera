package org.kesmarag.megaptera.linear

import org.kesmarag.megaptera.utils.toRowVector

class RowVector(_dimension: Int) : Vector(_dimension) {
    override fun t(): ColumnVector {
        val tmpVector = ColumnVector(dimension)
        for (i in 0..dimension - 1) {
            tmpVector[i] = this[i]
        }
        return tmpVector
    }

    override val type = VectorType.ROW_VECTOR

    operator fun times(other: ColumnVector): Double {
        if (dimension != other.dimension ) {
            throw IllegalArgumentException("vectors with different dimensions")
        }
        var innerProduct = 0.0
        for (i in 0..dimension - 1) {
            innerProduct += this[i] * other[i]
        }
        return innerProduct
    }

    override fun toString(): String {
        var str: String = "["
        for (i in 0..this.dimension-2){
            str = str + this[i].toString() + ", "
        }
        str+= "${this[this.dimension-1]}]\n"
        return str
    }

    override fun clone(): RowVector {
        val cloned = this.elements.toRowVector()
        return cloned
    }

    operator fun plus(other: ColumnVector): RowVector {
        if (this.dimension != other.dimension) {
            throw IllegalArgumentException("vectors with different dimensions")
        }
        val tmpVector = this.clone()
        for (i in 0..this.dimension - 1) {
            tmpVector[i] += other[i]
        }
        return tmpVector
    }

    operator fun plus(other: RowVector): RowVector {
        if (this.dimension != other.dimension) {
            throw IllegalArgumentException("vectors with different dimensions")
        }
        val tmpVector = this.clone()
        for (i in 0..this.dimension - 1) {
            tmpVector[i] += other[i]
        }
        return tmpVector
    }

    operator fun minus(other: RowVector): RowVector {
        if (this.dimension != other.dimension) {
            throw IllegalArgumentException("vectors with different dimensions")
        }
        return this + other * (-1.0)
    }

    operator fun times(num: Double): RowVector {
        val tmpVector = this.clone()
        for (i in 0..this.dimension - 1) {
            tmpVector[i] *= num
        }
        return tmpVector
    }

    operator fun times(num: Int): RowVector {
        val tmpVector = this.clone()
        for (i in 0..this.dimension - 1) {
            tmpVector[i] *= num.toDouble()
        }
        return tmpVector
    }
}

