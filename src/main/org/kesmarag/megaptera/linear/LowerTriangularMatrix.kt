package org.kesmarag.megaptera.linear

class LowerTriangularMatrix(_dimension: Int) : Matrix(_dimension, _dimension) {
    override val type: MatrixType = MatrixType.UPPER_TRIANGULAR
    private val elements = Array(rows) { i -> RowVector(i + 1)}

    operator fun get(i: Int, j: Int): Double {
        if (i >= j) {
            return elements[i][j]
        } else {
            return 0.0
        }
    }

    operator fun set(i: Int, j: Int, v: Double): Unit {
        if (i >= j) {
            elements[i][j] = v
        }
    }

    public fun t(): UpperTriangularMatrix {
        val transpose = UpperTriangularMatrix(rows)
        for (i in 0..rows - 1) {
            for (j in 0..i) {
                transpose[j, i] = this[i, j]
            }
        }
        return transpose
    }

    override fun toString(): String {
        var str = ""
        for (i in 0..rows-1){
            str += "["
            for (j in 0..columns-2){
                str += "${this[i,j]}, "
            }
            str += "${this[i,columns-1]}]\n"
        }
        //str+="\n"
        return str
    }


}