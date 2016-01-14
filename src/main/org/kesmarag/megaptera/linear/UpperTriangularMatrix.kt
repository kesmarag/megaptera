package org.kesmarag.megaptera.linear


class UpperTriangularMatrix(_dimension: Int) : Matrix(_dimension, _dimension) {
    override val type: MatrixType = MatrixType.UPPER_TRIANGULAR
    private val elements = Array(rows) { i -> RowVector(columns - i) }

    operator fun get(i: Int, j: Int): Double {
        if (i <= j) {
            return elements[i][j]
        } else {
            return 0.0
        }
    }

    operator fun set(i: Int, j: Int, v: Double): Unit {
        if (i <= j) {
            elements[i][j] = v
        }
    }


}