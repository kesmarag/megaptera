package org.kesmarag.megaptera.linear

import org.kesmarag.megaptera.utils.*
import org.kesmarag.megaptera.utils.toColumnVector
import org.kesmarag.megaptera.utils.toRowVector

class ColumnVector(_dimension: Int) : Vector(_dimension) {
    override fun t(): RowVector {
        val tmpVector = RowVector(dimension)
        for (i in 0..dimension - 1) {
            tmpVector[i] = this[i]
        }
        return tmpVector
    }

    override val type = VectorType.COLUMN_VECTOR

    override fun toString(): String {
        var str: String = "[\n"
        for (i in 0..this.dimension-2){
            str = str + this[i].toString() + " \n"
        }
        str+= "${this[this.dimension-1]}]\n"
        return str
    }

    override fun clone(): ColumnVector {
        val cloned = this.elements.toColumnVector()
        return cloned
    }



    operator fun plus(other: ColumnVector): ColumnVector {
        if (this.dimension != other.dimension) {
            throw IllegalArgumentException("vectors with different dimensions")
        }
        val tmpVector = this.clone()
        for (i in 0..this.dimension - 1) {
            tmpVector[i] += other[i]
        }
        return tmpVector
    }

    operator fun minus(other: ColumnVector): ColumnVector {
        if (this.dimension != other.dimension) {
            throw IllegalArgumentException("vectors with different dimensions")
        }
        return this + other// * (-1.0)
    }

    operator fun times(num: Double): ColumnVector {
        val tmpVector = this.clone()
        for (i in 0..this.dimension - 1) {
            tmpVector[i] *= num
        }
        return tmpVector
    }

    operator fun times(num: Int): ColumnVector {
        val tmpVector = this.clone()
        for (i in 0..this.dimension - 1) {
            tmpVector[i] *= num.toDouble()
        }
        return tmpVector
    }

}

