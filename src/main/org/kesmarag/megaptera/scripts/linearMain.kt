import org.kesmarag.megaptera.linear.*
import org.kesmarag.megaptera.utils.*

fun main(args: Array<String>) {
    val dense = DenseMatrix(30,30)
    val d = dense*dense
    val U = UpperTriangularMatrix(2)
    U[0,0] = 1.0 ; U[0,1] = 2.0 ; U[1,1] = 3.0
    val b = ColumnVector(2)
    b[0] = 1.0 ; b[1] = -1.0
    val x = U.solve(b)
    println(x)
}