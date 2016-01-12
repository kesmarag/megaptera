package org.kesmarag.megaptera.linear

public fun columnVectorOf(vararg _elements: Double): ColumnVector{
    val tmp = ColumnVector(_elements.size)
    for (i in 0.._elements.size - 1){
        tmp[i] = _elements[i]
    }
    return tmp
}

public fun rowVectorOf(vararg _elements: Double): RowVector{
    val tmp = RowVector(_elements.size)
    for (i in 0.._elements.size - 1){
        tmp[i] = _elements[i]
    }
    return tmp
}


