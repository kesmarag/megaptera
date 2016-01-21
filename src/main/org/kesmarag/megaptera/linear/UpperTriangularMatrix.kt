package org.kesmarag.megaptera.linear

class UpperTriangularMatrix(_dimension: Int) : Matrix(_dimension, _dimension) {
    override val type: MatrixType = MatrixType.LOWER_TRIANGULAR
    private val elements = Array(rows) { i -> RowVector(columns - i) }

    operator fun get(i: Int, j: Int): Double {
        if (i <= j) {
            return elements[i][j - i]
        } else {
            return 0.0
        }
    }

    operator fun set(i: Int, j: Int, v: Double): Unit {
        if (i <= j) {
            elements[i][j - i] = v
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
        for (i in 0..rows - 1) {
            for (j in 0..rows - 1) {
                var sum = 0.0
                for (k in 0..rows - 1) {
                    sum += this[i, k] * lower[k, j]
                }
                newDense[i, j] = sum
            }
        }
        return newDense
    }

    operator fun times(vector: ColumnVector) : ColumnVector{
        val a = ColumnVector(rows)
        for (i in 0..rows-1) {
            var sum = 0.0
            for (j in 0..columns - 1) {
                sum += this[i, j] * vector[j]
            }
            a[i] = sum
        }
        return a
    }

    public fun inv(): DenseMatrix {
        val im1 = DenseMatrix(rows, rows)
        var vec = ColumnVector(rows)
        var x = ColumnVector(rows)
        for (i in 0..rows - 1) {
            vec[i] = 1.0
            x = this.solve(vec)
            for (j in 0..rows - 1) {
                im1[j, i] = x[j]
            }
            vec[i] = 0.0
        }
        return im1
    }

    public fun solve(b: ColumnVector): ColumnVector {
        val x = ColumnVector(rows)
        for (i in rows - 1 downTo 0) {
            x[i] = b[i]
            for (j in i + 1..rows - 1) {
                x[i] = x[i] - this[i, j] * x[j]
            }
            x[i] = x[i] / this[i, i]
        }
        return x
    }

    override fun toString(): String {
        var str = ""
        for (i in 0..rows - 1) {
            str += "["
            for (j in 0..columns - 2) {
                str += "${this[i, j]}, "
            }
            str += "${this[i, columns - 1]}]\n"
        }
        //str+="\n"
        return str
    }

    public fun fromVector(vector: Vector): Unit {
        if (vector.dimension != (rows*rows+rows)/2){
            throw IllegalStateException("incompatible dimension of the vector")
        }
        var offset = 0
        var i = 0
        var k = 0


    }


}