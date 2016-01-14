import org.kesmarag.megaptera.linear.ColumnVector
import org.kesmarag.megaptera.linear.DenseMatrix
import org.kesmarag.megaptera.linear.columnVectorOf
import org.kesmarag.megaptera.utils.*

fun main(args: Array<String>) {


    val dense = DenseMatrix(5,5)
    val p = dense*dense
    val vector = columnVectorOf(0.3,3.2,1.21)
    val sig = softmax(vector)
    println("${sig[0]} ${sig[1]} ${sig[2]}")
}