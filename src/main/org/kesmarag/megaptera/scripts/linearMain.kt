import org.kesmarag.megaptera.linear.RowVector
import org.kesmarag.megaptera.linear.columnVectorOf
import org.kesmarag.megaptera.utils.distance

fun main(args: Array<String>) {
    val vec1 = columnVectorOf(1.0,2.0,3.0).t()
    val vec2 = columnVectorOf(4.0,5.0,6.0)
    println(vec1*vec2)
    println(distance(vec1,vec2))
}