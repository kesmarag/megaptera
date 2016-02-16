package org.kesmarag.megaptera.scripts

import org.kesmarag.megaptera.ann.MixtureDensity
import org.kesmarag.megaptera.ann.MixtureDensityNetwork
import org.kesmarag.megaptera.utils.DataSet
import org.kesmarag.megaptera.utils.toColumnVector
import java.util.*


fun main(args: Array<String>){
    val resultData = DataSet("/home/kesmarag/dev/megInv/results2.csv");
    val targetData = DataSet("/home/kesmarag/dev/megInv/parameters2.csv");
    val data = resultData[0].data
    val targets = targetData[0].data
    val trainingSize = data.size-2
    val mdn = MixtureDensityNetwork(7,200, 2, 7)
    val md = MixtureDensity(7, 2)

    mdn.trainingBatchSGD(data,targets,1,200000,0.01,5000)
   // val n1 = mdn.apply(data[840].data.toColumnVector())
    //md.hyperParameters(n1)
   // println(md)
    for (i in 0..1680) {
        val n2 = mdn.apply(data[i].data.toColumnVector())
        md.hyperParameters(n2)
        //println(targets[i].data.toColumnVector())
        //println(md)
        println(i)
        println(md.weights)
        //println(md.means[0])
       // println(md.means[1])

        //println(md)
    }
    //println()




}


