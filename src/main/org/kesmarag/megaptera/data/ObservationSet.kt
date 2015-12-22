package org.kesmarag.megaptera.utils

class ObservationSet(owner: Int = 0) : Property(owner) {
    public var data: MutableList<Observation> = arrayListOf()
        private set
    public var size: Int = 0
        private set
    public var scores: DoubleArray = DoubleArray(0)
    public var label: String = "none"
    public operator fun get(i: Int): Observation {
        return data[i]
    }

    public operator fun plusAssign(observation: Observation): Unit {
        data.add(observation)
        size++
    }

    public fun standardize(): Unit {
        var sArray: DoubleArray = DoubleArray(data[0].length)
        var sdArray: DoubleArray = DoubleArray(data[0].length)
        data.forEach {
            for (k in 0..data[0].length - 1) {
                sArray[k] += it[k]
            }
        }
        data.forEach {
            for (k in 0..data[0].length - 1) {
                sdArray[k] += (it[k]-sArray[k]/size.toDouble())*(it[k]-sArray[k]/size.toDouble())/size.toDouble()
            }
        }
        data.forEach {
            for (k in 0..data[0].length - 1) {
                it.data[k] = (it.data[k] - sArray[k] / size.toDouble()) / Math.sqrt(sdArray[k])
            }
        }

    }

}