import org.kesmarag.megaptera.ann.MixtureDensityNetwork
import org.kesmarag.megaptera.linear.*
import org.kesmarag.megaptera.utils.*

fun main(args: Array<String>) {
    val a = ColumnVector(6)
    a.randomize(0.0,1.0)
    val mdn = MixtureDensityNetwork(6,10,2,2)
    val y = mdn(a)
    val yHat = y//softmax(y)
   // println(yHat)

}