# Buổi 4: Generic, Extension Function, Scope Function
- [Buổi 4: Generic, Extension Function, Scope Function](#buổi-4-generic-extension-function-scope-function)
  - [I. Generic](#i-generic)
  - [II. Extension Function](#ii-extension-function)
  - [III. Scope Function:](#iii-scope-function)
    - [1. Mục đích và lợi ích](#1-mục-đích-và-lợi-ích)
    - [2. Điểm khác biệt giữa các scope function:](#2-điểm-khác-biệt-giữa-các-scope-function)
    - [3. Context Object:](#3-context-object)
    - [4. Return Value:](#4-return-value)
    - [5. Các scope function:](#5-các-scope-function)
      - [a. let:](#a-let)
      - [b. with](#b-with)
      - [c. run](#c-run)
      - [d. apply:](#d-apply)
      - [e. also](#e-also)
    - [6. takeIf \& takeUnless](#6-takeif--takeunless)

## I. Generic
- Định nghĩa: **Generic** cho phép các lớp, hàm hoặc thuộc tính có tham số kiểu. 
- Tức là ta có thể định nghĩa một lớp hoặc một hàm mà nó có thể hoạt động với nhiều loại dữ liệu khác nhau mà không cần phải viết lại mã cho từng loại. 
- Ví dụ: Tạo một lớp `Box<T>` mà `T` là một tham số kiểu, có thể là `Int`, `String` hoặc bất kỳ kiểu nào khác.
![](https://images.viblo.asia/full/fac9bb11-044e-432c-8d63-53572eceacfd.png)

- Ta sử dụng `T` để đại diện cho 1 kiểu dữ liệu chung chung. Ví dụ:
```kotlin
class Student<T>(private val data: T) {
    fun showData() {
        println(data)
    }
}

fun main() {
    Student("Hai").showData()
    Student(19).showData()
}
```



- **Generic** được sử dụng nhằm mục đích giải quyết vấn đề `type safety` và tăng tính linh hoạt, tái sử dụng mã:
  + Đảm bảo `type safety` & tránh lỗi Runtime:
        - Trong Kotlin, các kiểu **generic** mặc định là **bất biến**. Tức là `List<String>` không phải kiểu con của `List<Object>`.
          - Nếu generic không bất biến thì ta có thể gặp lỗi `ClassCastException` ở Runtime. Nếu `List<String>` là kiểu con của `List<Object>`, ta vô tình thêm kiểu `Integer` vào trong `List<String>` thông qua tham chiếu `List<Object>` dẫn đến lỗi đọc `String` từ danh sách đó.
  + Tăng tính linh hoạt, tái sử dụng mã:
     - Trong java: Với phương thức `addAll()` trong Collections, nếu nó được khai báo là `addAll(Collection<E> items)`, ta sẽ không thể sao chép 1 `Collection<String>` vào một `Collection<Object>`. Khi ấy ta giải quyết bằng cách dùng **wildcard types** như `? extends E` hoặc `? super E`.
      - Trong Kotlin, ta sẽ giải quyết thông qua **declaration-site variance** và **use-site variance**:
        - **Declaration-site variance** (Biến thiên tại nơi báo):
          - Kotlin cho phép khai báo một tham số kiểu `in` hoặc `out` trực tiếp tại nơi khai báo lớp hoặc giao diện.
          - Modifier `out` (convariant): 
            - Dùng để đánh dấu tham số kiểu `<T>`là convariant. Tức là chỉ trả về từ các hàm thành viên và không bao giờ được sử dụng.
            - Sử dụng từ khóa `out`
            - Gán giá trị của con cho cha
            ```kotlin
            open class Father()

            class Son() : Father()

            class Person<out T>(val value: T) {
                fun get(): T {
                    return value
                }
            }

            fun main(){
                val sonObject = Person(Son())
                val fatherObject: Person<Father>
                fatherObject = sonObject
            }
            ```
          - Modifier `in` (contravariant):
            - Dùng để đánh dấu tham số kiểu `<T>`là contravariant. Tức là chỉ được **consumed** (sử dụng) chứ không được **returned** (trả về).
            ```kotlin
            open class Father()

            class Son() : Father()

            class Person<in T>() {
                fun say(value : T){
                    println("${value.hashCode()}")
                }
            }


            fun main(){
                val fatherObject : Person<Father> = Person()
                val sonObject : Person<Son>
                sonObject = fatherObject
            }
            ```
        - **Use-site variance** (Biến thiên tại nơi sử dụng - Type projections):
          - Đối với các lớp không thể hoàn toàn là convariant hoặc contravariant như `Array<T>` (vì nó vừa có get() và set()).
            ```kotlin
            val ints: Array<Int> = arrayOf(1, 2, 3)
            val any = Array<Any>(3) { "" }

            copy(ints, any)  //expected Array<Any>, found Array<Int>
            ```
            Bị lỗi bởi vì nó vừa đọc và ghi kiểu T, nên không thể chỉ là `out T` hay `in T`.
          - Giải pháp là ta sẽ chỉ định rõ `out` hoặc `in` khi sử dụng chứ không phải khi khai báo class:
            ```kotlin
            fun copy(from: Array<out Any>, to: Array<Any>) {
                for (i in from.indices)
                    to[i] = from[i]  //from chỉ cho phép đọc
            }
            copy(ints, any)
            
            fun fill(dest: Array<in String>, value: String) {
                for (i in dest.indices)
                    dest[i] = value  //chỉ được ghi vào dest
            }
            fill(Array<Any>(3) { "" }, "hello")
            fill(Array<CharSequence>(3) { "" }, "hi")
            ```
        - **Star-projections**:
          - Star-projection (*) cho phép sử dụng kiểu generic mà không cần biết đối số kiểu cụ thể, nhưng vẫn đảm bảo an toàn kiểu dữ liệu.
          - Cách hoạt động:
            - `Foo<out T : TUpper>` → Foo<*> tương đương Foo<out TUpper> → Có thể đọc giá trị kiểu TUpper.
            - `Foo<in T> → Foo<*>`tương đương Foo<in Nothing> → Không thể ghi giá trị nào an toàn.
            - `Foo<T : TUpper>` (invariant) → Foo<*> tương đương:
              - Đọc như `Foo<out TUpper>`
              - Ghi như `Foo<in Nothing>`
            - Với generic có nhiều tham số, mỗi tham số có thể chiếu độc lập, ví dụ:
                ```kotlin
                interface Function<in T, out R>
                Function<*, String> → Function<in Nothing, String>

                Function<Int, *> → Function<Int, out Any?>

                Function<*, *> → Function<in Nothing, out Any?>
                ```    

- Một số quy ước trong Generics: Có rất nhiều cách đặt tên cho kiểu tham số trong Generic nhưng nên tuân theo kiểu đặt tiêu chuẩn để dễ dàng làm việc nhóm hơn:
  - T - type (KDL bất kỳ thuộc Wrapper Class: String, Integer, ...)
  - E – Element (phần tử – được sử dụng phổ biến trong Collection Framework)
  - K – Key (khóa)
  - V – Value (giá trị)
  - N – Number (kiểu số: Integer, Double, Float, …)

- Generic Function:
  - Ngoài các lớp, hàm cũng có thể có tham số kiểu. Các tham số kiểu được đặt trước tên hàm.
  - Để gọi **generic function**, ta sử dụng **type argument** sau tên hàm. Các **type argument** cũng có thể được bỏ qua nếu được suy ra:
  ```kotlin
  fun main() {
    val intTest = toString(345)
    println(intTest)

    val dbTest = toString<Double>(31.7)
    println(dbTest)
  }

  fun <T> toString(c: T): String {
    return c.toString()
  }
  ```

- Generic constraints - Ràng buộc generic:
  - Nhằm giới hạn các kiểu dữ liệu có thể được thay thế cho 1 tham số kiểu nhất định.
  - Loại ràng buộc phổ biến nhất là `upper bound`
    - `Type` sẽ được chỉ định sau dấu `:`
    - Ví dụ: `fun <T : Comparable<T>> sort(list: List<T>) { ... }`. Điều này có nghĩa là chỉ các kiểu là kiểu con của `Comparable<T>` mới có thể được thay thế cho `T`
    - Nếu không có ràng buộc trên nào được chỉ định thì mặc định là `Any?`
    - Chỉ có thể chỉ định 1 ràng buộc duy nhất bên trong dấu ngoặc nhọn `< >`
  - Nếu muốn chỉ định nhiều ràng buộc ta dùng `where`. Ví dụ:
  `fun <T> copyWhenGreater(list: List<T>, threshold: T): List<String> where T : CharSequence, T : Comparable<T>`
## II. Extension Function
- Giúp bổ sung chức năng mới cho class hoặc interface mà không cần phải kế thừa từ lớp đó hoặc sử dụng các design pattern như Decorator.
- Điều này giúp ta có thể tự viết các hàm mới cho class hoặc interface từ thư viện của 1 bên thứ 3 mà ta không thể sửa đổi.
- Các hàm này được gọi 1 cách thông thường, như thể chúng là phương thức của lớp gốc.
- Ngoài ra còn có "extension properties" (thuộc tính mở rộng) cho phép định nghĩa các thuộc tính mới cho các lớp hiện có.
- Cách khai báo 1 Extension Function:
  ```kotlin
  fun main() {
      val x = 13
      val checkNumber = CheckNumber()
      println(checkNumber.greaterThan10(x))
      println(checkNumber.lessThan10(x))
  }

  fun CheckNumber.lessThan10(x: Int): Boolean {
      return x < 10
  }

  class CheckNumber {
      fun greaterThan10(x: Int): Boolean {
          return x > 10
      }
  }
  ```
  Từ khóa this bên trong một extension function tương ứng với đối tượng nhận (đối tượng được truyền trước dấu chấm)
  ```kotlin
  fun main() {
      var str1 = "Team"
      val str2 = "Mobile"
      val str3 = "ProPTIT"
      //C1:
      str1 += (" $str2 $str3")
      println(str1)

      //C2:
      str1 = "Team"
      println(str1.add(str2, str3))
  }

  fun String.add(str2: String, str3: String): String {
      return "$this $str2 $str3"
  }
  ```
- Độ phân giải tĩnh (Static Resolution):
  - Các Extensions không thực sự thay đổi các class hoặc interface mà chỉ là mở rộng chúng, chỉ làm cho các hàm mới có thể được gọi được bằng dấu chấm trên các biến của kiểu này
  - Extension Function được phân phối tĩnh (static dispatched). Tức là Extension Function nào được gọi đã được biết tại thời điểm biên dịch trên kiểu khai báo của đối tượng nhận.
  ```kotlin
  open class Shape
  class Rectangle: Shape()

  fun Shape.getName() = "Shape"
  fun Rectangle.getName() = "Rectangle"

  fun printClassName(s: Shape) {
      println(s.getName())
  }

  fun main() {
      printClassName(Rectangle())
  }
  ```
  -> Mặc dù khởi tạo instance của Rectangle() nhưng mà bởi vì `printClassName` nhận vào là 1 đối tượng Shape nên hàm `getName()` sẽ gọi tới `getName()` của Shape chứ không phải của Rectangle.

- Ưu tiên khi có xung đột: Nếu 1 class có 1 hàm thành viên và 1 extensions function được định nghĩa cùng kiểu nhận, cùng tên và áp dụng được cho các đối số đã cho thì nó **ưu tiên đi tới hàm thành viên trước**
```kotlin
open class Shape {
    fun getName() = "MemberFunction Shape"
}
class Rectangle: Shape()

fun Shape.getName() = "Shape"
fun Rectangle.getName() = "Rectangle"

fun printClassName(s: Shape) {
    println(s.getName())
}

fun main() {
    printClassName(Rectangle())
}
```

- Receiver cũng có thể null:
  - Extensions có thể được định nghĩa với một kiểu nhận có thể là null.
  - Những extension này có thể được gọi trên một biến đối tượng ngay cả khi giá trị của nó là `null`
  - Nếu receiver là `null` thì this cũng là `null`. Chính vì vậy khi định nghĩa 1 extension với kiểu nhận có thể null thì cần check trường hợp `null` trước:
  ```kotlin
  fun Any?.toString(): String {
    if (this == null) return "null"
    return toString()
  }
  ```
- Extension Properties:
  - Kotlin hỗ trợ extension properties tương tự như extension functions. Ví dụ: `val <T> List<T>.lastIndex: Int get() = size - 1`
  - Vì extensions không thực sự chèn thành viên vào lớp -> 1 extension property không có backing field -> Initializers không cho phép với EP -> Phải định nghĩa = cách cung cấp getter/setter rõ ràng.

- Companion object extensions:
  - Nếu class có companion object -> Ta cũng có thể định nghĩa extension function hoặc property cho companion object đó
  ```kotlin
  class MyClass {
      companion object { }  // will be called "Companion"
  }

  fun MyClass.Companion.printCompanion() { println("companion") }

  fun main() {
      MyClass.printCompanion()
  }
  ```

- Phạm vi của extension:
  - Hầu hết ta định nghĩa extension tại top-level vì vậy khi cần dùng ở các package khác thì cần import nó
  ```kotlin
  package org.example.declarations

  fun List<String>.getLongestString() { /*...*/}

  ////////
  package org.example.usage

  import org.example.declarations.getLongestString

  fun main() {
      val list = listOf("red", "green", "blue")
      list.getLongestString()
  }
  ```

- Khai báo 1 extension như là 1 thành viên của lớp khác:
  - Bên trong một extension như vậy, có nhiều `implicit receivers` (đối tượng ngầm định) - các đối tượng mà thành viên của chúng có thể được truy cập mà không cần định danh
  - Một instance của lớp mà trong đó extension được khai báo được gọi là `dispatch receiver`.
  - Một instance của kiểu nhận của phương thức extension được gọi là `extension receiver`.
  - Trong trường hợp xung đột tên giữa các thành viên của `dispatch receiver` và `extension receiver`, `extension receiver` sẽ được ưu tiên.
  ```kotlin
  class Host(val hostname: String) {
      fun printHostname() { print(hostname) }
  }

  class Connection(val host: Host, val port: Int) {
      fun printPort() { print(port) }

      fun Host.printConnectionString() {
          printHostname()   // calls Host.printHostname()
          print(":")
          printPort()   // calls Connection.printPort()
      }

      fun connect() {
          /*...*/
          host.printConnectionString()   // calls the extension function
      }
  }

  fun main() {
      Connection(Host("kotl.in"), 443).connect()
      //Host("kotl.in").printConnectionString()  // error, the extension function is unavailable outside Connection
  } 
  ```
  - Để tham chiếu đến thành viên của `dispatch receiver`, ta sử dụng cú pháp qualified this (ví dụ: `this@Connection.toString()`)
  ```kotlin
  class Connection {
      fun Host.getConnectionString() {
          toString()         // calls Host.toString()
          thisAcongConnection.toString()  // calls Connection.toString()
      }
  }
  ```
## III. Scope Function:
- Là các hàm trong thư viện chuẩn của Kotlin để thực thi mã trong ngữ cảnh của một đối tượng. Khi gọi tới 1 scope function và cung cấp 1 biểu thức lambda, nó sẽ tạo ra 1 phạm vi tạm thời. Trong phạm vi ấy, ta có thể truy cập đối tượng mà không cần gọi tên của nó
- Có 5 scope function chính: `let`, `run`, `with`, `apply` và `also`. Các hàm đều thực thi 1 khối mã trên 1 đối tượng. Điểm khác biệt ở cách đối tượng này trở nên khả dụng bên trong khối mã và giá trị trả về của toàn bộ biểu thức

### 1. Mục đích và lợi ích
- Làm cho code dễ hiểu, dễ đọc hơn:
```kotlin
Person("Alice", 20, "Amsterdam").let {
    println(it)
    it.moveTo("London")
    it.incrementAge()
    println(it)
}
```
Hàm tương đương:
```kotlin
val alice = Person("Alice", 20, "Amsterdam")
println(alice)
alice.moveTo("London")
alice.incrementAge()
println(alice)
```

### 2. Điểm khác biệt giữa các scope function:
| **Hàm**                  | **Tham chiếu đối tượng** | **Giá trị trả về** | **Là Extension function?** | **Mục đích khuyến nghị**                                                                                       |
| ------------------------ | ------------------------ | ------------------ | -------------------------- | -------------------------------------------------------------------------------------------------------------- |
| `let`                    | `it`                     | Kết quả lambda     | Có                         | Thực thi lambda trên đối tượng **non-null**, giới thiệu biểu thức như biến cục bộ, gọi hàm trên chuỗi kết quả. |
| `run`                    | `this`                   | Kết quả lambda     | Có                         | Cấu hình đối tượng và tính toán kết quả.                                                                       |
| `run` *(không ngữ cảnh)* | –                        | Kết quả lambda     | Không                      | Chạy các câu lệnh khi cần biểu thức.                                                                           |
| `with`                   | `this`                   | Kết quả lambda     | Không                      | Nhóm các lời gọi hàm trên đối tượng, giới thiệu đối tượng trợ giúp.                                            |
| `apply`                  | `this`                   | Đối tượng ngữ cảnh | Có                         | Cấu hình đối tượng.                                                                                            |
| `also`                   | `it`                     | Đối tượng ngữ cảnh | Có                         | Thực hiện **các hiệu ứng phụ** (side effects), nhận đối tượng làm đối số trong lambda.                         |

### 3. Context Object:
- Bên trong `lambda` được truyền vào 1 `scope function`, `context object` sẽ được gọi bằng một tham chiếu ngắn gọn thay vì tên thực của object.
- Mỗi `scope function` sử dụng 1 trong 2 cách để tham chiếu tới `context object`:
  - `this`: lambda receiver
  - `it`: lambda argument
- `this`:
  -  Các hàm `run`, `with` và `apply` tham chiếu đối tượng ngữ cảnh bằng từ khóa `this`. Điều này có nghĩa là bên trong `lambda`, đối tượng có sẵn như thể nó là `this` trong một hàm thông thường của lớp
  -  Ta có thể bỏ qua `this` khi truy cập tới `members` của `receiver object`, như vậy sẽ làm cho code ngắn gọn hơn.
  -  Tuy nhiên, nếu `this` bị lược bớt, sẽ gây khó khăn trong việc phân biệt `receiver members` với các `objects` hay `functions` bên ngoài. 
  ```kotlin
  val adam = Person("Adam").apply { 
      age = 20                       // same as this.age = 20
      city = "London"
  }
  println(adam)
  ```
- `it`:
  - Các hàm `let` và `also` tham chiếu đối tượng ngữ cảnh như một đối số của `lambda`. Nếu tên đối số không được chỉ định, đối tượng sẽ được truy cập bằng tên mặc định ngầm định là `it`
  - `it` thường ngắn gọn hơn `this` và các biểu thức với `it` thường dễ đọc hơn.
  - Nên sử dụng `it` khi đối tượng chủ yếu được sử dụng làm đối số trong các lời gọi hàm, hoặc khi sử dụng nhiều biến trong khối mã. Ta cũng có thể đặt tên rõ ràng cho đối số thay vì sử dụng `it`
  ```kotlin
  fun getRandomInt(): Int {
      return Random.nextInt(100).also {
          writeToLog("getRandomInt() generated value $it")
      }
  }
  fun main() {
    val i = getRandomInt()
    println(i)
  }
  ```

### 4. Return Value:
- `apply` và `also` return the `context object`. Có thể tiếp tục sử dụng để gọi hàm trên cùng 1 object, lần lượt.
```kotlin
fun main() {
  val numberList = mutableListOf<Double>()
  numberList.also { println("Populating the list") }
      .apply {
          add(2.71)
          add(3.14)
          add(1.0)
      }
      .also { println("Sorting the list") }
      .sort()
}
```
- `let`, `run` và `with` return the `lambda result`. Dùng chúng để gán kết quả cho biến, tính toán kết quả, …
```kotlin
val numbers = mutableListOf("one", "two", "three")
val countEndsWithE = numbers.run { 
    add("four")
    add("five")
    count { it.endsWith("e") }
}
println("There are $countEndsWithE elements that end with e.")
```

### 5. Các scope function:
#### a. let:
- Context Object: `it`
- Return value: `Lambda result`
- Sử dụng khi:
  - Thực thi mã trên đối tượng `non-nullable`
  - Gọi 1 hoặc nhiều hàm trên kết quả của chuỗi lời gọi
  ```kotlin
  val numbers = mutableListOf("one", "two", "three", "four", "five")
  val resultList = numbers.map { it.length }.filter { it > 3 }
  println(resultList)    
  ```
  Ta có thể sử dụng `let` để gọi chuỗi mà không cần gán biến trung gian
  ```kotlin
  val numbers = mutableListOf("one", "two", "three", "four", "five")
  numbers.map { it.length }.filter { it > 3 }.let { 
      println(it)
      // and more function calls if needed
  } 
  ```
  Nếu khối mã được truyền vào `let` chứa 1 hàm duy nhất -> Sử dụng `::` thay cho `lambda argument`
  ```kotlin
  val numbers = mutableListOf("one", "two", "three", "four", "five")
  numbers.map { it.length }.filter { it > 3 }.let(::println)
  ```

#### b. with
- Context Object: `this`
- Return value: `Lambda result`
- Không phải là 1 `extension function`
- Sử dụng khi:
  - Nhóm các lời gọi hàm trên một đối tượng: Khi không cần sử dụng giá trị trả về của `with`. Có thể đọc là "với đối tượng này, hãy làm những điều sau".
  - Khi sử dụng `with`, cần đảm bảo rằng đối tượng đã được khởi tạo và không là `null`.
  ```kotlin
  val numbers = mutableListOf("one", "two", "three")
  with(numbers) {
      println("'with' is called with argument $this")
      println("It contains $size elements")
  }
  ```

#### c. run
- Context Object: `this`
- Return value: `Lambda result`
- Extension Function: Có thể có hoặc không
- Sử dụng khi:
  - Phiên bản extension (`object.run {}`): Hữu ích khi lambda vừa khởi tạo đối tượng vừa tính toán giá trị trả về
  ```kotlin
  //val service = MultiportService("https://example.kotlinlang.org", 80)

  val result = service.run {
      port = 8080
      query(prepareRequest() + " to port $port")
  }

  // the same code written with let() function:
  val letResult = service.let {
      it.port = 8080
      it.query(it.prepareRequest() + " to port ${it.port}")
  }
  ```
  - Phiên bản Non-extension (`run {}`): Không có đối tượng ngữ cảnh. Cho phép thực thi một khối gồm nhiều câu lệnh khi cần một biểu thức trả về. Có thể đọc là "chạy khối mã và tính toán kết quả"
  ```kotlin
  val hexNumberRegex = run {
      val digits = "0-9"
      val hexDigits = "A-Fa-f"
      val sign = "+-"

      Regex("[$sign]?[$digits$hexDigits]+")
  }

  for (match in hexNumberRegex.findAll("+123 -FFFF !%*& 88 XYZ")) {
      println(match.value)
  }
  ```

#### d. apply:
- Context Object: `this`
- Return value: `object itself`
- Sử dụng khi:
  - Cấu hình đối tượng: Trường hợp sử dụng phổ biến nhất. Đối với các khối mã không trả về giá trị và chủ yếu thao tác trên các thành viên của đối tượng receiver. Có thể đọc là "áp dụng các gán sau đây cho đối tượng".
  - Chuỗi nhiều lời gọi: Khi muốn tiếp tục chuỗi các thao tác trên cùng một đối tượng
  ```kotlin
  val adam = Person("Adam").apply {
      age = 32
      city = "London"        
  } 
  println(adam)
  ```

#### e. also
- Context Object: `it`
- Return value: `object itself`
- Sử dụng khi:
  -  Thực hiện các hành động phụ trợ (side effects): Hữu ích khi thực hiện một số hành động mà cần đối tượng ngữ cảnh làm đối số.
  - Khi cần tham chiếu đến đối tượng thay vì các thuộc tính của nó: Hoặc khi không muốn che khuất tham chiếu `this` từ một phạm vi bên ngoài. Có thể đọc là "và cũng làm điều sau đây với đối tượng"
  ```kotlin
  val numbers = mutableListOf("one", "two", "three")
  numbers
    .also { println("The list elements before adding new one: $it") }
    .add("four")
  ```

### 6. takeIf & takeUnless
- Cho phép nhúng các kiểm tra trạng thái của đối tượng vào chuỗi lời gọi
- `takeIf`: Trả về đối tượng nếu nó thỏa mãn một điều kiện (predicate); ngược lại trả về null. Đây là một hàm lọc cho một đối tượng duy nhất.
- `takeUnless`: Có logic ngược lại với `takeIf`. Trả về `null` nếu đối tượng thỏa mãn điều kiện; ngược lại trả về đối tượng
- Khi sử dụng `takeIf` hoặc `takeUnless`, đối tượng có sẵn dưới dạng đối số lambda `(it)`
```kotlin
val number = Random.nextInt(100)

val evenOrNull = number.takeIf { it % 2 == 0 }
val oddOrNull = number.takeUnless { it % 2 == 0 }
println("even: $evenOrNull, odd: $oddOrNull")
```
- Vì giá trị trả về của chúng có thể là null, cần thực hiện kiểm tra null hoặc sử dụng safe call `(?.)` khi chuỗi các hàm khác sau `takeIf` và `takeUnless`
```kotlin
val str = "Hello"
val caps = str.takeIf { it.isNotEmpty() }?.uppercase()
//val caps = str.takeIf { it.isNotEmpty() }.uppercase() //compilation error
println(caps)
```