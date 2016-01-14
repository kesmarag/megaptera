package org.kesmarag.megaptera.linear

class DenseMatrix(_rows: Int, _columns: Int) : Matrix(_rows, _columns) {
    override val type: MatrixType = MatrixType.DENSE
    private val elements = Array(rows) { i -> RowVector(columns) }

    operator fun get(i: Int): RowVector = elements[i]
    operator fun get(i: Int, j: Int): Double = elements[i][j]
    operator fun set(i: Int, j: Int, v: Double): Unit {
        elements[i][j] = v
    }

    public fun getColumn(j: Int): ColumnVector {
        val column = ColumnVector(rows)
        for (i in 0..rows - 1) {
            column[i] = elements[i][j]
        }
        return column
    }

    operator fun times(vector: ColumnVector): ColumnVector {
        val newVector = ColumnVector(rows)
        for (i in 0..rows - 1) {
            newVector[i] = elements[i] * vector
        }
        return newVector
    }

    operator fun times(other: DenseMatrix): DenseMatrix {
        val newDense = DenseMatrix(rows, other.columns)
        for (j in 0..other.columns - 1) {
            val column = getColumn(j)
            for (i in 0..rows - 1) {
                newDense[i][j] = this[i] * column
            }
        }
        return newDense
    }

    public fun t(): DenseMatrix{
        val newDense = DenseMatrix(columns,rows)
        for (i in 0..columns-1){
            for (j in 0..rows-1){
                newDense[i][j] = this[j][i]
            }
        }
        return newDense
    }

    override fun toString(): String {
        var str = ""
        for (i in 0..rows-1){
            str += this[i].toString()
        }
        str+="\n"
        return str
    }

}