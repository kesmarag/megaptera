package org.kesmarag.megaptera.scripts

import org.kesmarag.megaptera.ann.MixtureDensity
import org.kesmarag.megaptera.ann.MixtureDensityNetwork
import org.kesmarag.megaptera.utils.DataSet
import org.kesmarag.megaptera.utils.toColumnVector
import java.util.*


fun main(args: Array<String>){
    val resultData = DataSet("/home/kesmarag/dev/megInv/results.csv");
    val targetData = DataSet("/home/kesmarag/dev/megInv/parameters.csv");
    val data = resultData[0].data
    val targets = targetData[0].data
    val trainingSize = data.size-2
    val mdn = MixtureDensityNetwork(7, 20, 2, 1)
    val md = MixtureDensity(1, 2)

    mdn.trainingBatchSGD(data,targets,2,100000,0.02,500)
    val n1 = mdn.apply(data[861].data.toColumnVector())
    md.hyperParameters(n1)
    println(md)
    val n2 = mdn.apply(data[1500].data.toColumnVector())
    md.hyperParameters(n2)
    println(md)
    //println()




}


