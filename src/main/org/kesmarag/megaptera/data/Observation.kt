package org.kesmarag.megaptera.utils

import java.io.Serializable

class Observation : Property, Serializable{
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

    public operator fun get(i: Int): Double = data[i]


}