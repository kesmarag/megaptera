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

    public fun standardise(): Unit{
        if (length>1) {
            var mean = data.sum() / length.toDouble()
            var std = 0.0
            data.forEach { std += (it-mean) / (length-1).toDouble() }
            for (i in 0..length-1){
                data[i] = (data[i] - mean)/std
            }

        }else{

        }
    }

}