import org.kesmarag.megaptera.ann.MixtureDensityNetwork
import org.kesmarag.megaptera.linear.*
import org.kesmarag.megaptera.utils.*

fun main(args: Array<String>) {
    val a = ColumnVector(6)
    a.randomize(0.0,1.0)
    val mdn = MixtureDensityNetwork(6,10,2,1)
    val y = mdn.apply(a.t())
    val yHat = softmax(y)
    println(yHat)

}