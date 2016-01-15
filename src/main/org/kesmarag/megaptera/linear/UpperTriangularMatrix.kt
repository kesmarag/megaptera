package org.kesmarag.megaptera.linear

class UpperTriangularMatrix(_dimension: Int) : Matrix(_dimension, _dimension) {
    override val type: MatrixType = MatrixType.LOWER_TRIANGULAR
    private val elements = Array(rows) { i -> RowVector(columns - i) }

    operator fun get(i: Int, j: Int): Double {
        if (i <= j) {
            return elements[i][j-i]
        } else {
            return 0.0
        }
    }

    operator fun set(i: Int, j: Int, v: Double): Unit {
        if (i <= j) {
            elements[i][j-i] = v
        }
    }

    public fun t(): LowerTriangularMatrix {
        val transpose = LowerTriangularMatrix(rows)
        for (i in 0..rows - 1) {
            for (j in i..columns - 1) {
                transpose[j, i] = this[i, j]
            }
        }
        return transpose
    }

    operator fun times(lower: LowerTriangularMatrix): DenseMatrix {
        val newDense = DenseMatrix(rows, rows)
        for (i in 0..rows-1){
            for (j in 0..rows-1){
                var sum = 0.0
                for (k in 0..rows-1){
                    sum += this[i,k]*lower[k,j]
                }
                newDense[i,j] = sum
            }
        }
        return newDense
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