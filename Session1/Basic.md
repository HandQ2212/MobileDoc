# BUỔI 1: Kotlin cơ bản
- [BUỔI 1: Kotlin cơ bản](#buổi-1-kotlin-cơ-bản)
  - [I. Biến, kiểu dữ liệu](#i-biến-kiểu-dữ-liệu)
    - [a. Cách để khai báo biến:](#a-cách-để-khai-báo-biến)
    - [b. Kiểu dữ liệu:](#b-kiểu-dữ-liệu)
  - [II. Câu lệnh rẽ nhánh](#ii-câu-lệnh-rẽ-nhánh)
  - [III. Vòng lặp](#iii-vòng-lặp)
  - [IV. Các Colections trong Kotlin](#iv-các-colections-trong-kotlin)
  - [V. Null safety](#v-null-safety)
  - [VII. Nhập xuất:](#vii-nhập-xuất)


## I. Biến, kiểu dữ liệu

### a. Cách để khai báo biến:

- Để khai báo 1 biến, đầu tiên ta cần sử dụng 1 trong 2 từ khóa: 
  + `val` (value)   : biến không đổi - Biến được đặt bởi từ khóa này sẽ không được thay đổi giá trị.
  + `var` (variable): biến có thể thay đổi  - Ta có thể thay đổi giá trị của biến này.

- Demo:
  + Khai báo bằng cách không chỉ rõ kiểu dữ liệu:
    ```Kotlin
    var name = "Hai"
    var old = 13
    ```

  + Khai báo bằng cách chỉ rõ kiểu dữ liệu:
    ```Kotlin
    var name: String = "Hai"
    var old: Int = 13
    ```

  + Các kiểu khai báo trên là việc gán giá trị vào biến, nhưng ta cũng có thể chỉ định kiểu dữ liệu trước rồi sau đó mới gán giá trị của biến
    ```Kotlin
    var name: String
    //thuc hien 1 so cau lenh
    name = "Hai"
    ```

    *Lưu ý rằng phải chỉ định kiểu dữ liệu của biến với cách làm này nếu không bị lỗi giống như code ở dưới đây:*
    ```Kotlin
    var name    //ERROR vi chua biet duoc KDL cua name
    name = "Hai"
    ```

  + Đối với các biến được khai báo bởi từ khóa `val`, ta không thể thay đổi giá trị của biến đó sau khi đã được đặt giá trị khởi tạo:
    ```Kotlin
    val name = "Hai"
    name = "Nam"    //ERROR vi bien name duoc khoi tao bang tu khoa val
    ```

### b. Kiểu dữ liệu:
- KDL dạng số:
	* Số nguyên:
  
        | Kiểu dữ liệu  | Khoảng giá trị  |
        |---|---|
        | Byte  |-128 -> 127 (-2^7^ -> 2^7^ - 1)|
        | Short  |-32768 -> 32767  (-2^15^ -> 2^15^ - 1) |
        | Int  | -2147483648 -> 2147483647  (-2^31^ -> 2^31^ - 1)|
        | Long  | -9223372036854775808 -> 9223372036854775807 (-2^63^ -> 2^63^ - 1) |

	* Số thực:
		- Float: Lưu trữ 6 -> 7 chữ số sau dấu phẩy.
		- Double: Lưu trữ 15 -> 16 chữ số sau dấu phẩy
+ KDL đúng sai:
	+ Boolean: return true or false.

+ KDL kí tự:
	+ Char: trả về kí tự. Lưu ý là sẽ khác Java khi nhập thứ tự của kí tự trong ASCII sẽ bị lỗi.

+ Mảng trong Kotlin:
	+ Mảng là 1 thùng chứa dữ liệu (giá trị) của 1 KDL !.
	+ Mảng được đại diện bởi 1 class `Array`. Class sẽ có các hàm get, set, size và 1 số hàm khác.

+ Chuỗi trong Kotlin:
    + Chuỗi được đại diện bởi class `String`. Các chuỗi ký tự được triển khai như 1 instance của class `String`.

## II. Câu lệnh rẽ nhánh
- Giống `if else` ở trong Java
- Demo:
    ```kotlin
    val time = 20
    if (time < 12) {
        println("Good morning.")
    } else if (time < 20) {
        println("Good day.")
    } else {
        println("Good evening.")
    }
    ```
- Ta có thể sử dụng `if else` làm biểu thức gán
    ```kotlin
    val time = 15
    val greeting = if (time < 10) {
        "Good morning."
    } else {
        "Good evening."
    }
    println(greeting)
    ```
    hoặc có thể
    ```kotlin
    val time = 15
    val greeting = if (time < 10) "Good morning." else "Good evening"
    println(greeting)
    ```
- `when`: ý nghĩa giống như `switch-case`
Demo:
```kotlin
val day = 4
val result = when (day) {
  1 -> "Monday"
  2 -> "Tuesday"
  3 -> "Wednesday"
  4 -> "Thursday"
  5 -> "Friday"
  6 -> "Saturday"
  7 -> "Sunday"
  else -> "Invalid day."
}
println(result)
```
## III. Vòng lặp
- `while`:
Demo:
```kotlin
var i = 5
while (i > 1) {
  println(i)
  i--
}
```

- `for`:
  - **Duyệt tuần tự các giá trị trong danh sách - Closed Range:**
  ```kotlin
  for (i in a..b) {
    //xu ly bien i
  }
  ```
  -> i thuộc [a, b] với bước nhảy tăng dần 1 đơn vị từ a -> b.

  - **Duyệt tuần tự gần hết giá trị trong danh sách - half-open range:**
  ```kotlin
  for (i in a until b) {
    //xu ly bien i
  }
  ```
  -> i thuộc [a, b) với bước nhảy tăng dần 1 đơn vị từ a -> (b-1).

  - **Bước nhảy step:**
  ```kotlin
  for (i in a..b step x) {
    //Xu ly bien i
  }
  ```
  -> i tự động tăng dần từ a -> b với bước nhảy x. `step` cũng có thể sử dụng chung với các kiểu duyệt khác

  - **downTo:**
  ```kotlin
  for (i in b downTo a) {
    //Xu ly bien i
  }
  ```
  -> Duyệt i giảm dần 1 đơn vị từ b về a

  - **Lặp tập đối tượng:**
  ```kotlin
  fun main() {
    val nameList = arrayOf("An", "Hai", "Hoa", "Phuc")
    for (name in nameList) {
        print("$name ")
    }
  }
  ```
  hoặc cũng có thể duyệt kèm chỉ số:
  ```kotlin
  fun main() {
    val nameList = arrayOf("An", "Hai", "Hoa", "Phuc")
    for (i in nameList.indices) {
        println("STT $i co ten: " + nameList[i])
    }
  }
  ```
  Kotlin cũng hộ trợ việc vừa lấy index và value theo cách sau:
  ```kotlin
  fun main() {
    val nameList = arrayOf("An", "Hai", "Hoa", "Phuc")
    for ((index, value) in nameList.withIndex()) {
        println("STT $index co ten: $value" )
    }
  }
  ```

## IV. Các Colections trong Kotlin
- Collections bao gồm List, Set, Map
- Immutable: Các collection sẽ chỉ có thể thực hiện dọc mà không thể tác động vào giá trị của nó
```kotlin
fun main() {
    //Immutable:
    //List
    print("LIST: ")
    val sampleList: List<Int> = listOf(3, 4, 5, 6)
    for (element in sampleList) {
        print("${element} ")
    }
    println()

    //Set
    print("SET: ")
    val sampleSet: Set<Int> = setOf(3, 4, 3)
    for (element in sampleSet) {
        print("${element} ")
    }
    println("3 is in set: ${sampleSet.contains(3)}")
    println("5 is in set: ${sampleSet.contains(5)}")

    //Map
    print("MAP: ")
    val sampleMap: Map<Int, String> = mapOf(1 to "A", 2 to "B")
    for (element in sampleMap) {
        print("key = ${element.key} - value = ${element.value}; ")
    }
}
```

- Mutable: Có thể tác động vào các giá trị của các collection
```kotlin
fun main() {
    //Mutable:
    //MutableList
    println("MUTABLE_LIST: ")
    val sampleList: MutableList<Int> = mutableListOf(3, 4, 5, 6)
    print("Before: ")
    for (element in sampleList) {
        print("${element} ")
    }
    println()
    sampleList.add(7)
    sampleList.add(8)
    print("After add 2 number: ")
    for (element in sampleList) {
        print("${element} ")
    }
    println()
    print("After remove first number: ")
    sampleList.removeAt(0)
    for (element in sampleList) {
        print("${element} ")
    }
    println()
    println()

    //MutableSet
    println("MUTABLE_SET: ")
    print("Before: ")
    val sampleSet: MutableSet<Int> = mutableSetOf(3, 4, 3)
    for (element in sampleSet) {
        print("${element} ")
    }
    println()
    println("Check 3 & 5 is in set:")
    println("3 is in set: ${sampleSet.contains(3)}")
    println("5 is in set: ${sampleSet.contains(5)}")
    sampleSet.remove(3)
    print("After remove 3: ")
    for (element in sampleSet) {
        print("${element} ")
    }
    println()
    sampleSet.remove(8)
    print("After remove 8: ")
    for (element in sampleSet) {
        print("${element} ")
    }
    println()
    println()

    //MutableMap
    println("MUTABLE_MAP: ")
    print("Before: ")
    val sampleMap: MutableMap<Int, String> = mutableMapOf(1 to "A", 2 to "B")
    for (element in sampleMap) {
        print("key = ${element.key} - value = ${element.value}; ")
    }
    println()
    sampleMap.put(3, "C")
    print("After put {3, C}: ")
    for (element in sampleMap) {
        print("key = ${element.key} - value = ${element.value}; ")
    }
    println()

    sampleMap.remove(2)
    print("After remove key 2: ")
    for (element in sampleMap) {
        print("key = ${element.key} - value = ${element.value}; ")
    }
    println()
}
```
- 1 số hàm trong collection: [Hàm trong collection Kotlin](https://viblo.asia/p/tim-hieu-collections-list-set-map-trong-kotlin-ORNZqbLLl0n)

## V. Null safety
```kotlin
fun main() {
    val a: Int = null
    println(a.toString())
}
```
- Đối với cách khai báo a với giá trị null như trên, compiler sẽ báo về: Biến không được để null mà phải gán giá trị cho nó. Nhưng trong 1 số trường hợp, ta cần phải gán cho nó với null thì có thể sử dụng cách thêm dấu `?` vào cuối kiểu khai báo của nó:
```kotlin
fun main() {
    val a: Int? = null
    println(a.toString())
}
```

- Safe call: Khi gọi đến các biến có thể null, ta cần sử dụng 1 safe call:
```kotlin
fun main() {
    val str: String? = null
    val size = str?.length
    println(size)
}
```
Trong safe call, dấu `?` ở `str?` sẽ kiểm tra `str` có null hay không, nếu có thì sẽ không truy xuất tới `str` và `size` sẽ có giá trị null. Trường hợp còn lại `str` khác null thì sẽ trả về length của `str` -> Tránh lỗi `NullPointerException`

- Toán tử Elvis: Là toán tử `?:`, khá giống với toán tử ba ngồi, xem ví dụ để hiểu rõ hơn:
```kotlin
fun main() {
    var str: String? = null
    var size = str?.length ?: 0
    println(size)
}
```
-> Thay vì như lúc trước, khi `str` có giá trị null thì size sẽ được gán là null thì khi dùng toán tử Elvis, ta sẽ gán được giá trị của size = 0. Trong trường hợp `str` khác null thì cũng tương tự
  
## VII. Nhập xuất:
- Nhập
```kotlin
import java.utils.Scanner
fun main() {
  val reader = Scanner(System.`in`)

  val a = reader.toInt()
  pritnln(a)
}
```
- Xuất:
  - print(), println() như thường