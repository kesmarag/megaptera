package org.kesmarag.megaptera.scripts

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.kesmarag.megaptera.hmm.GhmmClustering
import org.kesmarag.megaptera.utils.DataSet
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.ObjectOutputStream

fun main(args: Array<String>) {
    println(".:: megaptera v0.1c ::.")
    //println(args[0])
    //val dataSet = DataSet("/home/kesmarag/dev/test/dataSet3/*.csv", 1)
    val dataSet = DataSet(args[0] + "/*.csv", 1)
    val clustering = GhmmClustering(dataSet, args[1].toInt(), args[2].toInt(), args[3].toInt())
    clustering.training()
    println("### Results ###")
    val fileOut = FileOutputStream(args[4] + "/clusters.ser")
    val out = ObjectOutputStream(fileOut)
    out.writeObject(clustering)
    out.close()
    fileOut.close()
    //var i_noisy: Int = 1682
    //dataSet.members.forEachIndexed { i, oSet -> if (oSet.label == "data_01682.csv") i_noisy = i }
    //println("i_noisy = ${i_noisy}")
    /*
    var min = 1000.0
    var argName: String = "none"
    for (i in 0..dataSet.size-2){
        val d = dist(dataSet.members[i].scores,dataSet.members[i_noisy].scores)
        if (d < min && dataSet.members[i].label != "data_01682.csv"){
            min = d
            argName = dataSet.members[i].label
        }
    }
    dataSet.members
            //.filter { it.label == "data_841.csv" || it.label == "data_1682.csv" || it.label == argName }
            .sortedBy { it.label }
            .forEachIndexed { i, oS ->
                println("${oS.label} => ${oS.ownerID} => ${oS.scores[0]} |" +
                        " ${oS.scores[1]} | ${oS.scores[2]}")
            }
    */
    //println("argmin = $argName")
    //dataSet.members
    //.filter { it.ownerID == 1 }
    //        .sortedBy { it.label }
    //        .forEachIndexed { i, oS -> println("${oS.label} => ${oS.ownerID}") }
    //dataSet.members.filter { it.ownerID == 2 }
    //        .sortedBy { it.label }
    //        .forEachIndexed { i, oS -> println("${oS.label} => ${oS.ownerID}") }
    // dataSet.members.filter { it.ownerID == 3 }
    //         .sortedBy { it.label }
    //         .forEachIndexed { i, oS -> println("${oS.label} => ${oS.ownerID}") }
    // println("=== Scores ===")
    // println("=== 0 ===")
    // dataSet[0].scores.forEach { println(it) }
    // println("=== 1 ===")
    // dataSet[1].scores.forEach { println(it) }
    val file = File(args[4] + "/results.csv")
    val writer = FileWriter(file)
    var records = CSVFormat.EXCEL
    val csvFilePrinter = CSVPrinter(writer, records)
    dataSet.members
            //.filter { it.label == "data_841.csv" || it.label == "data_1682.csv" || it.label == argName }
            .sortedBy { it.label }
            .forEachIndexed { i, oS ->
                //println(oS.label)
                csvFilePrinter.printRecord(oS.scores.toArrayList())
            }

    writer.flush();
    writer.close();
    csvFilePrinter.close();


}

fun dist(a1: DoubleArray, a2: DoubleArray): Double {
    var s: Double = 0.0
    for (n in 0..a1.size - 1) {
        s += (a1[n] - a2[n]) * (a1[n] - a2[n])
    }
    return s
}