import org.kesmarag.megaptera.ann.MixtureDensity
import org.kesmarag.megaptera.ann.MixtureDensityNetwork
import org.kesmarag.megaptera.linear.*
import org.kesmarag.megaptera.utils.*

fun main(args: Array<String>) {
    val a = ColumnVector(2)
    a.randomize(0.0,1.0)
    val mdn = MixtureDensityNetwork(2,3,2,2)
    val y = mdn(a)
    y.fill(0.0)
    //val yHat = softmax(y)
   // println(yHat)
    val d = MixtureDensity(2,2)
    d.hyperParameters(y)

    println(d.derivative(y))
}