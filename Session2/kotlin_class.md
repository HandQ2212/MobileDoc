# BUỔI 2: CLASS

- [BUỔI 2: CLASS](#buổi-2-class)
  - [I. Access Modifier - Chỉ định truy cập](#i-access-modifier---chỉ-định-truy-cập)
  - [II. Constructor - Chỉ định truy cập cho hàm khởi tạo:](#ii-constructor---chỉ-định-truy-cập-cho-hàm-khởi-tạo)
    - [1. Các loại constructor:](#1-các-loại-constructor)
    - [2. Primary constructor (hàm khởi tạo chính):](#2-primary-constructor-hàm-khởi-tạo-chính)
    - [3. Secondary constructor (hàm khởi tạo phụ):](#3-secondary-constructor-hàm-khởi-tạo-phụ)
    - [4. Kế thừa và super:](#4-kế-thừa-và-super)
    - [5. Constructor có thể đi cùng với các Access modifier:](#5-constructor-có-thể-đi-cùng-với-các-access-modifier)
  - [III. Init Block](#iii-init-block)
  - [IV. Class Members](#iv-class-members)
    - [3. Propertise - Thuộc tính:](#3-propertise---thuộc-tính)
    - [4. Nested and inner classes:](#4-nested-and-inner-classes)
      - [a. Nested class - Lớp lồng nhau:](#a-nested-class---lớp-lồng-nhau)
      - [b. Inner class - Lớp bên trong:](#b-inner-class---lớp-bên-trong)
    - [5. Object declarations](#5-object-declarations)
      - [a. Object Declaration (Singleton)](#a-object-declaration-singleton)
      - [b. Object Expression (Anonymous Object)](#b-object-expression-anonymous-object)
      - [c. Companion Object](#c-companion-object)
      - [d. Ưu điểm của object:](#d-ưu-điểm-của-object)
  - [V. Companion object](#v-companion-object)
  - [VI. Special Class:](#vi-special-class)
    - [a. Data class - Lớp dữ liệu:](#a-data-class---lớp-dữ-liệu)
    - [b. Enum class - Lớp liệt kê:](#b-enum-class---lớp-liệt-kê)
    - [c. Sealed class - Lớp bị giới hạn:](#c-sealed-class---lớp-bị-giới-hạn)
    - [d. So sánh:](#d-so-sánh)


## I. Access Modifier - Chỉ định truy cập
- Có 4 chỉ định truy cập: `private`, `protected`, `internal`, `public`. Mặc định sẽ là `public`
  - **Private**:
  Các khai báo với chỉ định `private` có thể được truy cập bên trong file/class chứa khai báo đó
  ```kotlin
    private const val numberThree = 3

    private class User() {
        private val numberEight = numberThree.plus(3)
    }
    // ERROR bởi vì biến numberEight cần truy cập được khai báo private trong class User
    private val numberEleven = numberEight.plus(numberThree)

  ```

  - **Protected**:
  Với `protected` các khai báo sẽ chỉ được truy cập trong file chứa khai báo và các class con (subclasses). `protected` không được phép cho các khai báo top-level (Hay là `protected` sẽ chỉ được sử dụng ở trong các class và subclasses), điều này là do top-level không có khái niệm kế thừa nên không sử dụng được
  ```kotlin
  //Protected.kt  file
  protected val tmp = 234 
  protected fun hiddenFunction() {
    print("Hidden Function")
  }
  // 2 phần khai báo trên sẽ bị ERROR bởi vì protected không được khai báo ở bên ngoài class

  open class Parent {
    protected val secret = "Protected"
    protected fun reveal() = print(secret)
  }

  class Child : Parent {
    fun show() {
        reveal()        //OK
        println(secret) //OK
    }
  }
  ```

  - **Internal**:
  Các khai báo được chỉ định truy cập `internal` có thể được truy cập mọi nơi trong cùng 1 module
  1 module là 1 tập các file Kotlin được biên dịch cùng nhau:
    - Một project IntelliJ hoặc Android Studio.
    - Một Gradle module (trong project Android đa module).
    - Một Maven module.
    - Một jar file đã biên dịch.
  Ví dụ về 1 module & internal:
  ```path
    MyProject/
    ├── app/             ← module chính (ứng dụng)
    │   └── Main.kt
    └── library/         ← module thư viện riêng biệt
        └── Utils.kt
  ```
  Module: `libary`
  ```kotlin
  // file: library/src/main/kotlin/com/example/library/Utils.kt
    package com.example.library

    internal class InternalHelper {
        fun help() = "Helping internally"
    }

    internal fun internalFunction(): String = "Only visible inside library module"
    
    internal fun show() {
        println(internalFunction())
    }    
  ```
  Module: `app`
  ```kotlin
    // file: app/src/main/kotlin/com/example/app/Main.kt
    package com.example.app

    import com.example.library.InternalHelper   // ERROR: 'InternalHelper' is internal
    import com.example.library.internalFunction // ERROR: 'internalFunction' is internal

    fun main() {
        val helper = InternalHelper()          // ERROR
        println(internalFunction())            // ERROR
    }
  ```

  - **Public**:
  Các khai báo với chỉ định truy cập `public` có thể được truy cập ở mọi nơi. `public` là chỉ định truy cập mặc định trong Kotlin

  ```kotlin
  // Public.kt file

  public const val numberThree = 3

  public open class User() {
    public val numberEight = numberThree.plus(3)
  }

  public class Moderator() {
    public val numberEleven = numberThree + User().numberEight
  }

  //Antother.kt file
  private const val numbeTwentyTwo = numberThree + User().numberEight + Moderator().numberEleven
  ```

- **Java vs Kotlin**:
  - Access Modifie trong Kotlin gần tương tự với Java, tuy nhiên có 1 số điểm khác biệt:
    - Chỉ định truy cập mặc định: Kotlin là `public`, Java là `default`.
    - `private` cho package trong Java không có giá trị tương đương trong Kotlin, gần nhất với nó là `internal`
    - Class và Interface có thể `private` trong Kotlin
    - Outer class không thể truy cập tới các thành viên `private` của inner class trong Kotlin
    ```kotlin
    class Outer {
        class Inner {
            private val secret = "This is private"

            fun reveal() = secret
        }

        fun tryAccess() {
            val inner = Inner()
            // println(inner.secret) // ERROR: Không truy cập được tới biến secret vì nó là kiểu private
            println(inner.reveal())  // OK: vì gọi qua hàm public
        }
    }
    ```
    - Nếu ghi đè một `protected` method, mà không ghi rõ `public` hay `protected`, thì nó giữ nguyên mức `protected`. Kotlin không tự động nâng lên `public` như Java.
    ```kotlin
    open class Parent {
        protected open fun greet() {
            println("Hello from Parent")
        }
    }

    class Child : Parent() {
        override fun greet() {  // không chỉ định modifier
            println("Hello from Child")
        }

        fun callGreet() {
            greet()  // OK: gọi được vì nó vẫn là protected
        }
    }

    fun main() {
        val c = Child()
        // c.greet() ERROR: 'greet' đang ở protected
        c.callGreet() // OK: OK
    }
    ```

## II. Constructor - Chỉ định truy cập cho hàm khởi tạo:
### 1. Các loại constructor:
Kotlin có 2 loại constructor:
| Loại  | Tên                   | Nơi khai báo                                     |
| ----- | --------------------- | ------------------------------------------------ |
| Chính | Primary Constructor   | Ở ngay sau tên lớp                               |
| Phụ   | Secondary Constructor | Bên trong thân class, dùng từ khóa `constructor` |

Constructor là hàm đặc biệt dùng để khởi tạo đối tượng (object) của một lớp (class). Kotlin hỗ trợ hai loại constructor:
### 2. Primary constructor (hàm khởi tạo chính): 
Là constructor chính, được khai báo ngay sau tên class
  ```kotlin
  class Person(val name: String, var age: Int)

  fun main() {
      val me = Person("Hai", 20)
      println("${me.name} is ${me.age}")
  }
  ```

### 3. Secondary constructor (hàm khởi tạo phụ): 
Được khai báo bằng construct trong thân của class. Dùng khi cần nhiều cách khởi tạo
  ```kotlin
  class Person{
      val name: String
      var age: Int

      constructor(name: String, age: Int) {
          this.name = name
          this.age = age
      }
  }

  fun main() {
      val me = Person("Hai", 20)
      println("${me.name} is ${me.age}")
  }
  ```

### 4. Kế thừa và super: 
Nếu class con kế thừa từ class cha thì phải gọi constructor của cha bằng `super()`
```kotlin
// Primary constructor
open class Animal(val name: String)

class Cat(name: String, val bread: String) : Animal(name)

// Secondary constructor
open class Animal(val name: String)

class Cat : Animal {
    constructor(name: String) : super(name)
}
```

### 5. Constructor có thể đi cùng với các Access modifier:
`Constructor không bao giờ có chỉ định truy cập yếu hơn class của nó.`

- **public**:
  - Sẽ là mặc định nếu không chỉ rõ
  - Constructor sẽ được gọi ở bất kỳ đâu trong project
  ```kotlin
  class Person(val name: String, var old: Int)

  //or

  class Person public constructor(val name: String, var old: Int)
  ```

- **private**:
  - Giới hạn việc tạo object chỉ trong class đó.

- **protected**:
  - Chỉ dùng được trong class `open` hoặc `abstract`, và chỉ class con mới được phép gọi.
  - Không thể dùng protected cho top-level class (class không nằm trong class khác).
  ```kotlin
    open class Base protected constructor(val name: String)

    class Derived(name: String) : Base(name) {
        fun greet() = println("Hello, $name")
    }
  ```
  `Base("Hi")` sẽ không thể gọi ngoài `Derived`

- **internal**:
  - Chỉ có thể gọi constructor từ cùng một module.
  - Hữu ích khi muốn ẩn constructor với code bên ngoài module. 
  ```kotlin
  class InternalUser internal constructor(val id: Int)
  ```
  có thể gọi `InternalUser(5)` từ trong cùng module, nhưng không thể từ module khác.

| Modifier    | Gọi từ **ngoài class**        | Gọi từ **subclass**        | Gọi từ **module khác** |
| ----------- | ----------------------------- | -------------------------- | ---------------------- |
| `public`    |  Có thể                      |  Có thể                   |  Có thể               |
| `private`   |  Không thể (chỉ trong class) |  Không thể                |  Không thể            |
| `protected` |  Không thể                   |  Có thể                   |  Không thể            |
| `internal`  |  Có thể (nếu cùng module)    |  Có thể (nếu cùng module) |  Không thể            |

## III. Init Block
- Init block là khối lệnh chạy ngay sau khi **Primary constructor** được chạy. Ta sẽ tìm hiểu với 4 trường hợp:
  - Không có constructor: Lúc này, ta vẫn tạo được init block bởi vì các class đều có constructor
  ```kotlin
  class Person {
    init {
        println("Create person")
    }
  }
  ```
  - Chỉ có **Primary Constructor**: Lúc này init block sẽ chạy ngay sau khi primary constructor khởi tạo
  ```kotlin
  class User(val name: String) {
        init {
            println("Khởi tạo user với tên: $name")
        }
    }

    fun main() {
        val me = User("Hai")
    }
  ```
  **OUTPUT**
  ```bash
  Khởi tạo user với tên: Hai
  ```
  - Có cả **Primary Constructor** & **Secondary Constructor**: Init block sẽ chỉ chạy 1 lần rồi mới tới **Secondary Constructor**
  ```kotlin
  class User(val name: String) {
    var age: Int = 0
    init {
        println("Khởi tạo user với tên: $name")
    }
    constructor(name: String, age: Int) : this(name) {
        this.age = age
        println("User có tuổi: $age")
    }
        
    init {
        if (age == 0) println("Chua nhap tuoi") else println("Tuoi: $age")
    }
  }

  fun main() {
    val me = User("Hai", 20)
  }
  ```
  **OUTPUT**:
  ```bash
    Khởi tạo user với tên: Hai
    Chưa nhập tuổi
    User có tuổi: 20
  ```
  Lý do init block thứ 2 lại hiện `Chưa nhập tuổi` là bởi Kotlin luôn gọi primary constructor trước, tức là `this(name)` → chạy init block. Sau đó mới đến phần thân ở **secondary constructor** chạy.
  - Chỉ có **secondary constructor**: Bởi class luôn có **primary constructor** ẩn khi không khai báo, vậy nên init block sẽ chạy trước khi chạy tới **secondary constructor**:
    ```kotlin
    class User(val name: String) {
        var age: Int = 0

        init {
            println("Khởi tạo user với tên: $name")
        }

        constructor(name: String, age: Int) : this(name) {
            this.age = age
            println("User có tuổi: $age")
        }
            
        init {
            if (age == 0) println("Chua nhap tuoi") else println("Tuoi: $age")
        }
    }

    fun main() {
        val me = User("Hai", 20)
    }
    ```

    **OUTPUT**:
    ```bash
        Init block được gọi
        Secondary constructor: Hai
    ```
- Ta có thể viết nhiều init block và sẽ chạy tuần tự từ trên xuống theo code
- Lý do dùng:
  - Để chạy code khởi tạo phức tạp:
  ```kotlin
  class User(val name: String, val age: Int) {
    init {
        require(age >= 0) { "Tuổi không được âm!" }
        println("Tạo user: $name, tuổi: $age")
    }
  }
  ```
  - Dùng khi class có logic khởi tạo mà không thể xử lý ngay trong constructor:
  ```kotlin
  class Circle(val radius: Double) {
    val area: Double

    init {
        area = Math.PI * radius * radius
    }
  }
  ```
  - Gắn kết dữ liệu khởi tạo với các thuộc tính phức tạp:
  ```kotlin
  class Person(name: String) {
    val upperName: String

    init {
        upperName = name.uppercase()
    }
  }
  ```
## IV. Class Members
| Thành phần                   | Mô tả ngắn                                                    |
| ---------------------------- | ------------------------------------------------------------- |
| Constructors and init blocks | Constructor chính (primary), phụ (secondary), và `init block` |
| Functions                    | Các hàm thành viên của lớp (`fun`)                            |
| Properties                   | Biến thành viên (`val` / `var`) & getter, setter              |
| Nested and inner classes     | Lớp lồng trong (`nested` và `inner class`)                    |
| Object declarations          | Khai báo `object` trong lớp (singleton hoặc companion object) |


### 3. Propertise - Thuộc tính:
`val` -> read-only - Chỉ đọc
`var` -> Mutable - Có thể thay đổi
```kotlin
class Person(val name: String, var age: Int) {
    var isAdult: Boolean = age >= 18
}
```

- Getter & Setter:
  - Mặc định:
  Khi tạo 1 propery: `var name: String = "abc"`
  Kotlin ngầm hiểu:
  ```kotlin
  get() = field
  set(value) {
    field = value
  }
  ```
  `field` là từ khóa đặc biệt đại diện cho giá trị lưu trữ thực sự của thuộc tính.

  - Tùy biến:
  ```kotlin
    class User {
        var age: Int = 0
            get() {
                println("Lay tuoi: $field")
                return field
            }
            set(value) {
                field = if(value < 0) 0 else value
                println("Gan tuoi: $field")
            }
    }
    fun main() {
        val u = User()
        u.age = 6
        println(u.age)
    }
  ```

  - Khi sử dụng từ khóa `val` cho property thì chỉ dùng được getter, không có setter.
### 4. Nested and inner classes:
`Đều là định nghĩa lớp bên trong 1 lớp khác nhưng mà có sự khác nhau về cách hoạt động và truy cập vào lớp cha`

#### a. Nested class - Lớp lồng nhau:
- Không sử dụng từ khóa `inner`. Trong kotlin, nested class là mặc định `static` tức là không có tham chiếu tới class ngoài (outer class)
- Cách khai báo:
```kotlin
class Outer {
    val outerProperty = "Outer Property"

    class Nested {
        fun nestedFunction(): String {
            return "Hello from Nested Class"
        }
    }
}
```
Nested là lớp được khai báo bên trong Outer. Bởi vì không có `inner` nên nested class sẽ không thể truy cập tới outerProperty

- Cách sử dụng:
```kotlin
fun main() {
    val nested = Outer.Nested()
    println(nested.nestedFunction())
}
```
Không cần tạo `outer = Outer()` mà có thể gọi trực tiếp `Outer.Nested()`

- Nested class có thể có property, constructor, function riêng:
```kotlin
class Outer {
    class Nested(val x: Int) {
        fun square(): Int = x * x
    }
}

fun main() {
    val nested = Outer.Nested(5)
    println(nested.square()) // Output: 25
}
```

- Nested class lồng nhau:
```kotlin
class A {
    class B {
        class C {
            fun greet() = "Hi from class C"
        }
    }
}

fun main() {
    val c = A.B.C()
    println(c.greet()) // Output: Hi from class C
}
```
- Ta có thể Modifier cho Nested class như class bình thường


#### b. Inner class - Lớp bên trong:
`Được đánh dấu bằng từ khóa inner. Giữ nguyên tham chiếu tới outer class`

- Cách khai báo:
```kotlin
class Outer {
    val name = "Outer Class"

    inner class Inner {
        fun greet() = "Hello from $name"
    }
}
```

- Cách sử dụng;
```kotlin
fun main() {
    val outer = Outer()
    val inner = outer.Inner()
    println(inner.greet()) // Output: Hello from Outer Class
}
```

- `this@Outer` - Tham chiếu lớp ngoài:
```kotlin
class Outer {
    val name = "Outer"

    inner class Inner {
        val name = "Inner"

        fun printNames() {
            println("Inner name: $name")
            println("Outer name: ${thisACongOuter.name}")
        }
    }
}
```

- inner class có thể gọi hàm từ outer class:
```kotlin
class Outer {
    fun outerMethod() = "Outer method"

    inner class Inner {
        fun callOuter() = outerMethod()
    }
}

fun main() {
    val inner = Outer().Inner()
    println(inner.callOuter()) // Output: Outer method
}
```

- Dùng `super@Outer` khi kế thừa
```kotlin
open class Base {
    open fun hello() = "Hello from Base"
}

class Outer : Base() {
    inner class Inner {
        fun sayHello() = super@Outer.hello()
    }
}
```

### 5. Object declarations
- `object` dùng để khai báo 1 đối tượng duy nhất (singleton) mà không cần tạo class rồi new instance

#### a. Object Declaration (Singleton)
- Tạo đối tượng duy nhất tồn tại trong toàn chương trình.
- Thường dùng để khai báo các helper, config, hoặc class chứa static method (giống Java).
```kotlin
object AppConfig {
    val version = "1.0"
    fun printInfo() {
        println("App Version: $version")
    }
}

fun main() {
    AppConfig.printInfo()  // Output: App Version: 1.0
}
```

#### b. Object Expression (Anonymous Object)
- Dùng để tạo đối tượng vô danh (anonymous) – tương tự như class ẩn danh trong Java.
- Có thể kế thừa interface hoặc abstract class ngay tại chỗ.
```kotlin
val listener = object : Runnable {
    override fun run() {
        println("Running...")
    }
}
listener.run()
```

#### c. Companion Object
- Kotlin không có static như Java, nên dùng companion object để thay thế.
- Dùng trong class để khai báo các thành phần cấp lớp (class-level).
```kotlin
class MyClass {
    companion object {
        const val TAG = "MyClass"
        fun log(msg: String) = println("$TAG: $msg")
    }
}

fun main() {
    MyClass.log("Hello")  // Output: MyClass: Hello
}
```

#### d. Ưu điểm của object:
- Viết gọn singleton hơn Java.
- Không cần khởi tạo bằng constructor.
- Có thể chứa cả property và function.
- companion object giúp giữ logic static trong class.

## V. Companion object
- Chính bởi việc trong Kotlin không có `static` như trong Java nên ta sử dụng `companion object`. Nó dùng để chứa các class-level như hằng só, hàm tiện ích, factory method, .... Có thể gọi trực tiếp qua class mà không cần khởi tạo đối tượng
- ví dụ:
```kotlin
class MyClass {
    companion object Save{
        const val TAG = "MyClass"
        fun log(msg: String) {
            println("$TAG: $msg")
        }
    }
}

fun main() {
    MyClass.log("Hello!") // Output: MyClass: Hello!
}
```

- Companion Object implements Interface:
```kotlin
interface Loggable {
    fun log(msg: String)
}

class Logger {
    companion object : Loggable {
        override fun log(msg: String) {
            println("Log: $msg")
        }
    }
}

fun main() {
    Logger.log("Testing") // Output: Log: Testing
}
```

- Không thể truy cập thuộc tính private của class:
```kotlin
class MyClass {
    private val secret = "Top Secret"

    companion object {
        fun tryAccess() {
            // println(secret) // ERROR: Cannot access 'secret'
        }
    }
}
```

## VI. Special Class:
### a. Data class - Lớp dữ liệu:
`Dùng để lưu trữ dữ liệu và tự động sinh các hàm như equals(), hashCode(), toString(), copy()...`
```kotlin
data class User(val name: String, val age: Int)
fun main() {
    val u1 = User("Hai", 20)
    val u2 = u1.copy(age = 21)

    println(u1) 
    println(u2) 

    val (name, age) = u2
    println("$name is $age years old")
}

```

- Yêu cầu:
  - Phải có ít nhất 1 val hoặc var trong constructor chính
  - Không thể open, abstract, sealed hoặc inner
- Tự động có:
  - `toString()`
  - equals() giúp so sánh nội dung của hai đối tượng thay vì địa chỉ bộ nhớ.
  - hashCode() giúp đối tượng hoạt động đúng trong các cấu trúc hash như HashMap, HashSet.
  ```kotlin
  val u1 = User("Hai", 20)
  val u2 = User("Hai", 20)

  println(u1 == u2) // Output: true → so sánh giá trị
  println(u1 === u2) // Output: false → so sánh địa chỉ
  ```
  - `copy()`: Tạo ra bản sao của đối tượng, cho phép thay đổi một số giá trị thuộc tính mà không ảnh hưởng bản gốc
  ```kotlin
  val u1 = User("Hai", 20)
  val u2 = u1.copy(age = 21)

  println(u2) // Output: User(name=Hai, age=21)
  ```
  - `componentN()`: Tự động sinh component1(), component2(),... tương ứng với thứ tự các thuộc tính
  ```kotlin
  val user = User("Hai", 20)
  val (name, age) = user

  println(name) // Output: Hai
  println(age)  // Output: 20
  ```

### b. Enum class - Lớp liệt kê:
`Dùng để định nghĩa một tập hợp các hằng số có thể có giá trị cố định (giống enum trong Java).`

```kotlin
enum class Direction {
    NORTH, SOUTH, EAST, WEST
}

fun navigate(dir: Direction) {
    when (dir) {
        Direction.NORTH -> println("Go up")
        Direction.SOUTH -> println("Go down")
        else -> println("Go sideways")
    }
}
```

- Enum với properties và methods:
```kotlin
enum class Day(val isWeekend: Boolean) {
    MONDAY(false),
    SATURDAY(true),
    SUNDAY(true);

    fun describe() = if (isWeekend) "Relax!" else "Work hard!"
}

fun main() {
    println(Day.SATURDAY.describe()) // Output: Relax!
}
```

- Hàm có sẵn:
  - `values()`: Trả về danh sách các giá trị
  - `valueOf("NAME")`: Trả về enum theo tên
```kotlin
enum class Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

fun main() {
    val allDays = Day.values()
    println("Tất cả các ngày trong tuần:")
    for (day in allDays) {
        println(day)
    }

    println()

    val today = Day.valueOf("MONDAY")
    println("Hôm nay là: $today")
}
```
**OUTPUT**
```bash
Tất cả các ngày trong tuần:
MONDAY
TUESDAY
WEDNESDAY
THURSDAY
FRIDAY
SATURDAY
SUNDAY

Hôm nay là: MONDAY
```

### c. Sealed class - Lớp bị giới hạn:
`sealed class dùng để giới hạn tập hợp các lớp con có thể kế thừa nó. Nó được dùng phổ biến để mô phỏng kiểu dữ liệu đóng (closed type hierarchy), ví dụ như trạng thái (Loading, Success, Error), sự kiện, v.v`

```kotlin
sealed class Result

class Success(val data: String) : Result()
class Error(val message: String) : Result()
object Loading : Result()
```

- Tính chất

| Tính năng                        | Mô tả                                                               |
| -------------------------------- | ------------------------------------------------------------------- |
| **Giới hạn kế thừa**             | Chỉ có thể kế thừa sealed class **bên trong cùng một file**.        |
| **Tương thích với `when`**       | Khi dùng với `when`, không cần `else` nếu liệt kê đủ các class con. |
| **Không thể khởi tạo trực tiếp** | `sealed class` là **abstract**, không thể tạo instance trực tiếp.   |
| **Hữu ích cho trạng thái**       | Rất phù hợp để đại diện cho các trạng thái (state) rõ ràng.         |

```kotlin
sealed class NetworkState {
    object Loading : NetworkState()
    data class Success(val data: String) : NetworkState()
    data class Error(val errorMessage: String) : NetworkState()
}

fun handle(state: NetworkState) {
    when (state) {
        is NetworkState.Loading -> println("Đang tải dữ liệu...")
        is NetworkState.Success -> println("Thành công: ${state.data}")
        is NetworkState.Error -> println("Lỗi: ${state.errorMessage}")
        // Không cần `else` vì đã bao phủ hết các class con
    }
}
```

- Cần dùng khi:
  - Khi có một tập hợp trạng thái cố định, không thay đổi.
  - Khi muốn dùng when để phân nhánh theo loại dữ liệu một cách an toàn và đầy đủ.
  - Khi muốn có cả data class và object trong cùng một hệ thống phân loại.

### d. So sánh:
| Loại class     | Có thể kế thừa ngoài file? | Dùng cho trạng thái? | Hữu ích trong `when` |
| -------------- | -------------------------- | -------------------- | -------------------- |
| `data class`   |  Có                       |  Không lý tưởng     |  Không đặc biệt     |
| `enum class`   |  Không                    |  Tốt cho hằng số    |  Có                 |
| `sealed class` |  Không                    |  Rất phù hợp        |  Rất mạnh mẽ        |
