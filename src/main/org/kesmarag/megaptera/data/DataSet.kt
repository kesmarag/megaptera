package org.kesmarag.megaptera.utils

import org.apache.commons.csv.CSVFormat
import java.io.File
import java.io.FileReader
import java.io.Reader
class DataSet(filePattern: String = "*.csv", var candidateOwners: Int = 1) {
    public var members: MutableList<ObservationSet> = arrayListOf()
        private set
    public var size: Int = 0
        private set
    public var observationLength: Int = 0
        private set
    init {
        load(filePattern)
        redistribute()
        println("DataSet size = $size")
    }
    public operator fun get(q: Int): ObservationSet {
        return members[q]
    }
    private fun load(filePattern: String): Unit {
        val lof: List<File> = listOfFiles(filePattern)
        size = lof.size
        for (file in lof){
            var observationSet = ObservationSet(candidateOwners)
            val reader: Reader = FileReader(file)
            var records = CSVFormat.EXCEL.parse(reader)
            for (r in records){
                val rSize = r.size()
                if (observationLength == 0){
                    observationLength = rSize
                }
                if (rSize!=observationLength){
                    throw IllegalArgumentException("observation with " +
                            "different length in file ${file.absolutePath.toString()}")
                }
                val tmpArray = DoubleArray(rSize)
                for (j in 0..rSize-1){
                    tmpArray[j] = r[j].toDouble()
                }
                observationSet+=Observation(tmpArray)
            }
            observationSet.label = file.name
            members.add(observationSet)

        }
    }

    public fun redistribute(){
        members.forEach { it.redistribute(candidateOwners,0) }
    }

    public fun standardize(): Unit{
        members.forEach { it.standardize() }
    }

    override fun toString(): String {
        val str = "DataSet(size=$size,observationLength=$observationLength,candidateOwners=$candidateOwners)"
        return str
    }
}