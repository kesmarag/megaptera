package org.kesmarag.megaptera.linear

import org.kesmarag.megaptera.utils.toColumnVector
import org.kesmarag.megaptera.utils.toRowVector
import java.io.Serializable

abstract class Vector : Serializable, Cloneable {
    public val dimension: Int
    private var elements: DoubleArray
    abstract public val type: VectorType

    constructor(_dimension: Int) {
        dimension = _dimension
        elements = DoubleArray(_dimension)
    }

    operator fun set(i: Int, v: Double) {
        elements[i] = v
    }

    operator fun get(i: Int) = elements[i]

    operator fun plus(other: Vector): Vector {
        if (this.dimension != other.dimension) {
            throw IllegalArgumentException("vectors with different dimensions")
        }
        val tmpVector = this.clone()
        for (i in 0..this.dimension - 1) {
            tmpVector[i] += other[i]
        }
        return tmpVector
    }

    operator fun minus(other: Vector): Vector {
        if (this.dimension != other.dimension) {
            throw IllegalArgumentException("vectors with different dimensions")
        }
        return this + other * (-1.0)
    }

    operator fun times(num: Double): Vector {
        val tmpVector = this.clone()
        for (i in 0..this.dimension - 1) {
            tmpVector[i] *= num
        }
        return tmpVector
    }

    operator fun times(num: Int): Vector {
        val tmpVector = this.clone()
        for (i in 0..this.dimension - 1) {
            tmpVector[i] *= num.toDouble()
        }
        return tmpVector
    }

    override fun clone(): Vector {
        val cloned: Vector
        if (this.type == VectorType.COLUMN_VECTOR) {
            cloned = this.elements.toColumnVector()
        } else {
            cloned = this.elements.toRowVector()
        }
        return cloned
    }

    public fun randomize(a: Double, b: Double) {

    }

    abstract public fun t(): Vector

    public fun norm0(): Double = elements.map { Math.abs(it) }.max()!!

    public fun norm1(): Double {
        var result = 0.0
        elements.forEach { result += Math.abs(it) }
        return result
    }

    public fun norm2(): Double {
        var result = 0.0
        elements.forEach { result += it * it }
        return Math.sqrt(result)
    }

    operator public fun get(range: IntRange): Vector {
        val vector: Vector
        if (this.type == VectorType.COLUMN_VECTOR) {
            vector = ColumnVector(range.count())
        } else {
            //if(this.type == VectorType.ROW_VECTOR){
            vector = RowVector(range.count())
        }
        val firstOfRange = range.start
        for (i in range) {
            vector[i - firstOfRange] = this[i]
        }
        return vector
    }

}

