import org.kesmarag.megaptera.linear.DenseMatrix


fun main(args: Array<String>) {


    val dense = DenseMatrix(5,5)
    val p = dense*dense
    println(p[0])


}