import org.kesmarag.megaptera.ann.MixtureDensity
import org.kesmarag.megaptera.ann.MixtureDensityNetwork
import org.kesmarag.megaptera.linear.ColumnVector
import org.kesmarag.megaptera.linear.columnVectorOf

fun main(args: Array<String>) {
    val a = ColumnVector(4)
    val a1 = ColumnVector(4)
    val out = ColumnVector(2)
    val out1 = ColumnVector(2)
    val mdn = MixtureDensityNetwork(4, 25, 2, 2)
    val sample1 = columnVectorOf(0.2, 0.2, 0.2, 0.2)
    val sample2 = columnVectorOf(0.6, 0.4, 0.5, 0.4 )
    val md = MixtureDensity(2, 2)
    //md.hyperParameters(mdn(sample2))
    //    println("prior = ${md[sample2]}")
    var lambda = 0.005
    for (i in 0..100000) {
        a.randomize(0.0, 0.4)
        a1.randomize(0.4, 0.6)
        out.randomize(0.095, 0.105)
        out1.randomize(0.5, 1.0)
        var par = lambda * (5000.0 / (5000.0 + i.toDouble()))
        //println(par)
        mdn.adaptOne(a, out, par)
        mdn.adaptOne(a1, out1, par)
        md.hyperParameters(mdn(sample2))
        //println("posterior = ${md[mdn]} -- ${md[mdn(sample2)]}")
    }
    println(mdn(sample2))
    /*
    println(mdn(sample2))
    println(mdn.W1)
    println(mdn.W2)
    md.hyperParameters(mdn(sample2))
    println("posterior = ${md[sample2]}")*/
}