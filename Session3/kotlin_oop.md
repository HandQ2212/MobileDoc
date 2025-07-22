# 4 tính chất của OOP và Backing Field

- [4 tính chất của OOP và Backing Field](#4-tính-chất-của-oop-và-backing-field)
  - [I. 4 Tính chất của OOP:](#i-4-tính-chất-của-oop)
    - [a. Encapsulation - Tính đóng gói:](#a-encapsulation---tính-đóng-gói)
    - [b. Inheritance - Tính kế thừa](#b-inheritance---tính-kế-thừa)
    - [c. Polymorphism - Tính đa hình](#c-polymorphism---tính-đa-hình)
    - [d. Abstraction](#d-abstraction)
  - [II. Backing Field:](#ii-backing-field)

## I. 4 Tính chất của OOP:
### a. Encapsulation - Tính đóng gói:
- Là cách để che dấu những tính chất xử lý bên trong của đối tượng, những đối tượng khác không thể tác động trực tiếp làm thay đổi trạng thái chỉ có thể tác động thông qua các phương thức có thể truy cập từ bên ngoài của đối tượng đó.
- Sử dụng các `Access Modifier` hoặc các getter, setter để thể hiện được tính đóng gói
```kotlin
open class Person(
    private var name: String,
    private var age: Int
) {
    private var address: String = "Unknown"

    fun getAddress() = address
    fun setAddress(newAddress: String) {
        if (newAddress.isNotBlank()) address = newAddress
        else println("Invalid address!")
    }

    fun getName() = name
    fun setName(newName: String) {
        if (newName.isNotBlank()) name = newName
        else println("Invalid name!")
    }

    fun getAge() = age
    fun setAge(newAge: Int) {
        if (newAge in 0..150) age = newAge
        else println("Invalid age!")
    }
}


class Hai(age: Int) : Person("Hai", age)

fun main() {
    val p = Hai(20)
    // p.address = "asdkfj" //khong the truy cap truc tiep duoc toi address
    p.setAddress("Hanoi") //dung setter
    println("Address: " + p.getAddress())
    println()
    println("Before set new name: " + p.getName()) // Hanoi
    p.setName("abcd")
    println("After set new name: " + p.getName()) // abcd
    println()
    println("Before set new age: " + p.getAge()) // 20
    p.setAge(30)
    println("After set new age: " + p.getAge()) // 30
}
```
- Ngoài đóng gói các thuộc tính thì ta cũng có thể đóng gói các thuộc tính. Lúc này, khi 1 task cần nhiều bước tạo thành thì các task con đó ta sẽ để private và để public với task to để bên ngoài có thể truy cập vào
```kotlin
class UserManager {
    fun registerUser(name: String) {
        if (!validateUserData(name)) {
           println("Invalid User")
           return
        }
        connectToDB()
        println("Register User: $name")
    }

    fun logIn(name: String) {
        connectToDB()
        println("Log in: $name")
    }

    private fun connectToDB() {
        println("Connecting to Database...")
        println("Connected to Database")
    }

    private fun validateUserData(name: String): Boolean {
        return name.isNotBlank()
    }
}
```
### b. Inheritance - Tính kế thừa
- Tính kế thừa là việc tạo 1 class mới dựa trên 1 class đã có, khi đó class mới sẽ có thể sử dụng các properties hoặc methods của class được kế thừa. Ngoài ra class mới cũng có thể thêm các properties hoặc methods mới đặc trưng của nó.
- Tất cả các class đều kế thừa từ class `Any`, trong class `Any` sẽ có 3 phương thức: `equals()` , `hashCode()` , `toString()`. Vì vậy các class sẽ đều có 3 phương thức này
- Trong kotlin, các class mặc định ở `final` nghĩa là không thể kế thừa. Vì vậy, nếu dùng kế thừa thì class được kế thừa cần được đánh dấu `open` vào đằng trước class.
- Single level Inheritance - Kế thừa 1 cấp: 1 class con kế thừa từ 1 class cha. Class con sẽ kế thừa các properties hoặc methods từ class cha.
- Multiple level Inheritance - Kế thừa nhiều cấp: class ông > class bố > class con. Các properties hoặc methods trong class ông thì class con vẫn có thể sử dụng được.
```kotlin
open class Vehicle {
    fun startEngine() {
        println("Vehicle start engine")
    }
}

open class Car(private val brand: String) : Vehicle() {
    fun drive() {
        println("Driving a $brand car")
    }
}

class ElectricCar(brand: String,
                  private val batteryCapacity: Int) : Car(brand) {
    fun charge() {
        println("Charging $brand with $batteryCapacity kWh battery")
    }
}

class GasCar(brand: String,
             private val fuelTankSize: Int) : Car(brand) {
    fun refuel() {
        println("Refueling $brand with $fuelTankSize liters of gas.")
    }
}

fun main() {
    val tesla = ElectricCar("Tesla", 100)
    val toyota = GasCar("Toyota", 50)

    println("=== Electric Car ===")
    tesla.startEngine()   // Vehicle
    tesla.drive()         // Car
    tesla.charge()        // ElectricCar

    println("\n=== Gas Car ===")
    toyota.startEngine()  // Vehicle
    toyota.drive()        // Car
    toyota.refuel()       // GasCar
}
```
### c. Polymorphism - Tính đa hình
- Tính đa hình - Polymorphism thể hiện qua việc các Class khác nhau, kế thừa chung 1 class or interface có thể cùng gọi đến hàm (cùng giao diện/Interface) nhưng lại có thể thực hiện các logic khác nhau tùy vào nơi khai báo.
- **Method Overloading** (Compile-time Polymorphism): `Overload` là việc định nghĩa các phương thức (hàm) trong Class có cùng tên nhưng khác nhau về các input params
    ```kotlin
    class AddNumber {
        fun add (a: Int = 0, b: Int = 0) = a + b
        fun add (a: Double = 0.0, b: Double = 0.0) = a + b
        fun add (a: Int = 0, b: Int = 0, c: Int = 0) = a + b + c
    }
     ```
- **Method Overriding** (Run time polymorphism): Định nghĩa các phương thức (hàm) trong Class con được kế thừa từ Class cha, và thay đổi hoàn toàn logic bên trong nó.
  - Override với `open class`:
    ```kotlin
    open class BaseClass {
        open var name: String = "Base class"
        open fun print() {
            println("Hello from Base class")
        }
    }

    class SubClass : BaseClass() {
        override var name: String = "Sub class"
            get() = super.name
            set(value) {
                field = value
            }
        override fun print() {
            super.print()
            println("Hello from Sub class")
        }
    }

    fun main() {
        val base = BaseClass()
        val sub = SubClass()

        println("=======Base Class=======")
        println(base.name)
        base.print()

        println("=======Sub Class=======")
        println(sub.name)
        sub.print()
    }
    ```
  - Override với `interface`: 
    ```kotlin
    interface MyInterface {
        val name: String
        fun myFunction() { // Ham duoc dinh nghia san
            println("Hello from MyFunction")
        }
        fun myEmptyFunction() // Ham chua duoc dinh nghia
    }

    class MyClass : MyInterface {
        override val name: String = "MyClass"
        override fun myFunction() {
            super.myFunction()
            println("Hello from MyClass")
        }

        override fun myEmptyFunction() {
            println("Hello from myEmptyFunction")
        }
    }

    fun main() {
        val c = MyClass()
        println(c.name)
        c.myFunction()
        c.myEmptyFunction()
    }
    ```
- Q&A:
  - Điều khác biệt giữa kế thừa từ Class và Interface là gì?
    - Class chỉ có thể kế thừa từ 1 Class, nhưng có thể implement nhiều Interface.
  - Khi không muốn cho các module ngoài kế thừa Class, Interface bạn cần làm gì?
    - Sử dụng sealed Class, Interface
  - Trong Kotlin abstract class và class khác biệt gì?
    - class có thể được dùng để tạo object trực tiếp, abstract class thì không được.
    - Interface, abstract Class vẫn có thể tạo được object thông qua object expression. Cách này không nên áp dụng cho các inteface, abstract class lớn. Vì sẽ làm code trở nên phức tạp, khó đọc, khó debug.
### d. Abstraction
- Tính trừu tượng được thể hiện thông qua việc khi thiết kế các tính năng của phần mềm các Lập trình viên sẽ tập trung vào việc thiết kế luồng làm việc của các hành vi (behaviour / method) rồi từ đó phân chia ra các Class mà chưa đi sâu vào các chi tiết cụ thể. Các Class được thiết kế ở giai đoạn này gọi là Abstract Class, hoặc cũng có thể gọi là Interface trong 1 vài trường hợp.
- Trong Kotlin ta sử dụng `interface` hoặc `abstract class` để đạt tới tính trừu tượng.
- `Abstract class`:
  - Sử dụng từ khóa `abstract` để khai báo `Abstract class`
  - Các thuộc tính của `abstract class` mặc định là `non-abstract` trừ khi ta khai báo `abstract` rõ ràng. Khi được khai báo `abstract` các class kế thừa bắt buộc phải override lại các thuộc tính có khai báo `abstract`
  - Không cần sử dụng `open` bởi các `abstract class` luôn mở
    ```kotlin
    abstract class Animal {
        abstract fun makeSound()

        fun breathe() {
            println("Animal is breathing")
        }
    }

    class Dog : Animal() {
        override fun makeSound() {
            println("Woof!")
        }
    }
    ```
  - **Lưu ý**: 1 class chỉ được kế thừa từ 1 `abstract class`
- `interface`:
  - Tất cả các hàm trong `interface` mặc định là `abstract` vì vậy các class kế thừa từ `interface` đều cần cung cấp cách triển khai cụ thể cho các thuộc tính hoặc phương thức
    ```kotlin
    interface MyInterface {
        val name: String // abstract property
        fun myFunc() = println("Non-Empty")
        fun myEmptyFunc() // abstract method
    }

    class MyClass : MyInterface {
        override val name: String = "Hai"
        override fun myEmptyFunc() {
            println("Empty Function")
        }
    }

    fun main() {
        val hai = MyClass()
        println(hai.name)
        hai.myFunc()
        hai.myEmptyFunc()
    }
    ```
  - 1 class có thể được kế thừa từ nhiều interface
    ```kotlin
    interface Flyable {
        fun fly()
    }

    interface Swimmable {
        fun swim()
    }

    class Duck : Flyable, Swimmable {
        override fun fly() {
            println("Duck is Flying")
        }
        override fun swim() {
            println("Duck is Swimming")
        }
    }

    fun main() {
        val duck = Duck()
        duck.fly()
        duck.swim()
    }
    ```
    Khi kế thừa từ nhiều class, ta có thể gặp trường hợp các interface có thể bị trùng thuộc tính hoặc phương thức, khi đó ta sẽ giải quyết xung đột như ví dụ dưới đây:
    ```kotlin
    interface A {
        fun greet() {
            println("Hello from A")
        }
    }

    interface B {
        fun greet() {
            println("Hello from B")
        }
    }

    class C : A, B {
        override fun greet() {
            println("Resolving conflict...")
            super<A>.greet()  
            super<B>.greet()  
        }
    }

    fun main() {
        val c = C()
        c.greet()
    }
    ```

## II. Backing Field:
- Backing field là một cơ chế dùng để lưu trữ dữ liệu thực sự đằng sau một property, thường được dùng khi bạn muốn tùy chỉnh getter hoặc setter nhưng vẫn cần một biến để lưu giá trị.
- Cú pháp:
```kotlin
var name: String = "Hai"
    get() = field.uppercase() //HAI
    set(value) {
        field = value.trim()
    }
```