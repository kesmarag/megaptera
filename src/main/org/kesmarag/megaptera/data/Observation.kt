package org.kesmarag.megaptera.utils

class Observation : Property{
    public var data: DoubleArray
    public val length: Int
    constructor(d: DoubleArray, owner:Int = 0) : super(owner){
        data = d
        length = data.size
    }
    constructor(l: List<Double>, owner:Int = 0) : super(owner){
        data = l.toDoubleArray()
        length = data.size
    }
    public fun data(): DoubleArray{
        return data
    }

    public operator fun get(i: Int): Double{
        return data[i]
    }


}