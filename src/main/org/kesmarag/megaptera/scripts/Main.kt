package org.kesmarag.megaptera.scripts

import org.kesmarag.megaptera.hmm.GhmmClustering
import org.kesmarag.megaptera.utils.DataSet
import java.io.FileOutputStream
import java.io.ObjectOutputStream

fun main(args: Array<String>) {
    println(".:: megaptera v0.1a ::.")
    //val pat: String? = readLine()
    val dataSet = DataSet("/home/kesmarag/dev/test/dataSet/*.csv", 1)
    //val hmm = GaussianHiddenMarkovModel(dataSet, 4, 8, 0)
    //hmm.update()
    val clustering = GhmmClustering(dataSet, 4, 2, 1)
    clustering.training()
    println("### Results ###")
    val fileOut = FileOutputStream("/home/kesmarag/clusters.ser")
    val out = ObjectOutputStream(fileOut)
    out.writeObject(clustering)
    out.close()
    fileOut.close()
    dataSet.members
            .filter { it.label == "data_101.csv" || it.label == "data_202.csv" }
            .sortedBy { it.label }
            .forEachIndexed { i, oS ->
                println("${oS.label} => ${oS.ownerID} => ${oS.scores[0]} |" +
                        " ${oS.scores[1]} | ${oS.scores[2]}")
            }
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
}