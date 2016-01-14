import org.kesmarag.megaptera.linear.ColumnVector
import org.kesmarag.megaptera.linear.DenseMatrix
import org.kesmarag.megaptera.linear.columnVectorOf
import org.kesmarag.megaptera.utils.*

fun main(args: Array<String>) {
    val dense = DenseMatrix(5,5)
    dense[0,0] = 2.0
    dense[1,2] = 3.0
    dense[2,1] = -8.0
    val p = dense*dense*dense*dense*dense
    val vector = columnVectorOf(4.0,4.2,2.9,3.0,1.0)
    //val sig: ColumnVector = softmax(vector)
    //val vec = dense*sig
    println(vector)
}