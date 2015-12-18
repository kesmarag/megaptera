package org.kesmarag.megaptera.utils

class ObservationSet(owner: Int = 0) : Property(owner){
    public var data: MutableList<Observation> = arrayListOf()
        private set
    public  var size: Int = 0
        private set
    public var scores: DoubleArray = DoubleArray(0)
    public operator fun get(i: Int): Observation{
        return data[i]
    }
    public operator fun plusAssign(observation: Observation): Unit{
        data.add(observation)
        size++
    }

}