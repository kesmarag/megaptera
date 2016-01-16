package org.kesmarag.megaptera.linear

import java.io.Serializable

abstract class Matrix: Serializable, Cloneable {
    public val rows: Int
    public val columns: Int
    abstract public val type: MatrixType

    constructor(_rows: Int, _columns: Int){
        rows = _rows
        columns = _columns
    }

}