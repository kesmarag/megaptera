package org.kesmarag.megaptera.linear

import org.kesmarag.megaptera.utils.toColumnVector
import org.kesmarag.megaptera.utils.toRowVector
import java.io.Serializable
import java.util.*

abstract class Vector : Serializable, Cloneable {
    public val dimension: Int
    public var elements: DoubleArray
        private set
    abstract public val type: VectorType

    constructor(_dimension: Int) {
        dimension = _dimension
        elements = DoubleArray(_dimension)
    }

    operator fun set(i: Int, v: Double) {
        elements[i] = v
    }

    operator fun get(i: Int) = elements[i]


    public fun randomize(a: Double, b: Double) {
        for (i in 0..dimension-1) {
            this[i] = Random().nextDouble()*(b-a)+a
        }
    }

    abstract public fun t(): Vector

    public fun norm0(): Double = elements.map { Math.abs(it) }.max()!!

    public fun norm1(): Double {
        var result = 0.0
        elements.forEach { result += Math.abs(it) }
        return result
    }

    public fun sumOfSquares(): Double {
        var result = 0.0
        elements.forEach { result += it * it }
        return result
    }

    public fun norm2(): Double {
        var result = 0.0
        elements.forEach { result += it * it }
        return Math.sqrt(result)
    }



    operator public fun get(range: IntRange): ColumnVector {
        val vector: ColumnVector
        vector = ColumnVector(range.count())
        val firstOfRange = range.start
        for (i in range) {
            vector[i - firstOfRange] = this[i]
        }
        return vector
    }
}

