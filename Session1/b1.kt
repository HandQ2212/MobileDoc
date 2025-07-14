import kotlin.random.Random

const val max_element = 1000005;
fun solve(n: Int) {
    val sum = n*(n+1)/2
    if (sum%2 != 0) {
        println("NO")
        return
    }

    val half = sum/2
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()
    var count = 0

    for (i in n downTo 1) {
        if (count + i <= half) {
            list1.add(i)
            count += i
        } else list2.add(i)
    }

    if (count != half) {
        println("NO")
        return
    }

    println("YES")
    for (number in list1) {
        print("$number ")
    }
    println()
    for (number in list2) {
        print("$number ")
    }
}

fun main() {
    val n = Random.nextInt(1, max_element)
    solve(n)
}