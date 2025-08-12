# Buổi 9: Fragment
---
## I. Fragment, FragmentManager, FragmentTransaction

![](https://media.geeksforgeeks.org/wp-content/uploads/20201112160326/FragmentInteractionwithActivity.png)

### a. Fragment:
- **Fragment** thể hiện giao diện người dùng có thể tái sử dụng của ứng dụng. Mỗi **Fragment** xác định và quản lý bố cục riêng, có vòng đời riêng và có thể xử lý các sự kiện đầu vào riêng.
- Các **Fragment** không thể tồn tại độc lập mà cần `hosted` (gán) với 1 activity hoặc 1 fragment khác.
- Fragments cho phép chia UI thành các thành phần riêng biệt, từ đó UI được phân chia theo module và có thể tái sử dụng. 
![Hai phiên bản của cùng một màn hình trên nhiều kích thước màn hình. Ở bên trái, màn hình lớn chứa ngăn điều hướng do hoạt động kiểm soát và danh sách dạng lưới do mảnh kiểm soát. Ở bên phải, màn hình nhỏ chứa thanh điều hướng phía dưới cùng do hoạt động kiểm soát và danh sách tuyến tính do mảnh kiểm soát](https://developer.android.com/static/images/guide/fragments/fragment-screen-sizes.png?hl=vi)

- Sự khác nhau giữa Activity và Fragment:

| Tiêu chí                | **Activity**                                                                                       | **Fragment**                                                                                                         |
| ----------------------- | -------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------- |
| **Bản chất**            | Là một thành phần (component) độc lập của ứng dụng Android, có vòng đời riêng do hệ thống quản lý. | Là một phần của giao diện hoặc logic bên trong Activity; không thể tồn tại độc lập.                                  |
| **Quản lý giao diện**   | Mỗi Activity thường gắn với **một layout chính**.                                                  | Có thể thay đổi linh hoạt giao diện (UI) trong cùng Activity bằng cách thay thế các Fragment.                        |
| **Tính tái sử dụng**    | Ít linh hoạt hơn trong việc tái sử dụng UI vì thường gắn chặt với cấu trúc riêng.                  | Tính linh hoạt cao, có thể tái sử dụng cùng một Fragment ở nhiều Activity hoặc trong ViewPager, BottomNavigation,... |
| **Giao tiếp**           | Các Activity giao tiếp với nhau qua `Intent`.                                                      | Fragment giao tiếp với Activity hoặc Fragment khác thông qua **interface**, **ViewModel**, hoặc `FragmentManager`.   |
| **Chuyển đổi màn hình** | Mỗi lần chuyển Activity → UI phải load lại toàn bộ, tốn tài nguyên hơn.                            | Chuyển giữa Fragment **nhanh hơn** vì vẫn nằm trong cùng Activity, chỉ thay đổi một phần UI.                         |
| **Dùng khi**            | Dùng khi màn hình thực sự độc lập (Login, MainActivity).                                           | Dùng khi muốn chia nhỏ giao diện hoặc tái sử dụng UI logic (Tab, danh sách + chi tiết,...).                          |


### b. FragmentManager:
- `FragmentManager` là class chịu trách nhiệm thực hiện hành động của các Fragment của ứng dụng. Ví dụ: Thêm, sửa, xóa hoặc thay thế fragment, thêm fragment vào backstack.
- Truy cập vào `FragmentManager`:
  - `FragmentActivity` hoặc các lớp con của nó (như `AppCompatActivity`) có quyền truy cập vào `FragmentManager` thông qua `getSupportFragmentManager()`.
  - Các **Fragment** có thể chứa 1 hoặc nhiều **Fragment** con. Ta có thể lấy thông tin tham chiếu đến `FragmentManager` quản lý fragment con đó thông qua `getChildFragmentManager()`.
  - Nếu cần truy cập vào `FragmentManager` của host fragment đó thì dùng `getParentFragmentManager()`.
  ![](https://developer.android.com/static/images/guide/fragments/manager-mappings.png?hl=vi)
![Hai ví dụ về bố cục giao diện người dùng cho thấy mối quan hệ giữa mảnh và hoạt động của máy chủ.](https://developer.android.com/static/images/guide/fragments/fragment-host.png?hl=vi)

- Hình 1 cho thấy hai ví dụ, mỗi ví dụ có một máy chủ hoạt động duy nhất. Hoạt động của máy chủ trong cả hai ví dụ này hiển thị thành phần điều hướng cấp cao nhất cho người dùng dưới dạng `BottomNavigationView` chịu trách nhiệm hoán đổi mảnh máy chủ có nhiều màn hình trong ứng dụng. Mỗi màn hình được triển khai dưới dạng một mảnh riêng.

- Mảnh máy chủ trong Ví dụ 1 lưu trữ hai mảnh con tạo thành một màn hình xem phân tách. Mảnh máy chủ trong Ví dụ 2 lưu trữ một mảnh con duy nhất tạo thành mảnh hiển thị của khung hiển thị vuốt.

- **Sử dụng `FragmentManager`**:
  - `FragmentManager` quản lý backstack của **Fragment**. Trong runtime, nó quản lý các thao tác với backstack như thêm, xóa các fragment để phản hồi tương tác người dùng. Mỗi tập hợp thay đổi được xác nhận cùng nhau dưới dạng một đơn vị duy nhất gọi là `FragmentTransaction`.
  - Khi người dùng ấn nút back hoặc gọi `FragmentManager.popBackStack()`, fragment ở trên cùng sẽ bật ra khỏi backstack. Nếu backstack trống và không dùng child fragment thì sự kiện Back sẽ đc đẩy lên activity và lúc này sẽ back activity thay vì fragment. Nếu như dùng child fragment thì cần xử lý đặc biệt cho back stack của child fragments trước khi cho sự kiện Back đi lên Activity.
    - Ví dụ không dùng child fragment:
    ```bash
    MainActivity
    └── Fragment A  (addToBackStack)
        → Fragment B (addToBackStack)
    ```
    Bấm Back từ B → quay lại A (vì back stack có transaction).
    Bấm Back từ A → Activity đóng (vì back stack rỗng, sự kiện Back tới Activity).

    - Ví dụ dùng child fragment:
    ```bash
    MainActivity
    └── Fragment A
        └── Fragment A1 (child fragment)
                → Fragment A2 (child fragment)
    ```
    Bấm Back từ A2 → quay lại A1 (pop back stack của child fragment).
    Nếu child fragment back stack rỗng → mới chuyển sự kiện Back lên Activity.
  - Khi gọi `addToBackStack()` thì có nghĩa ta sẽ nhớ lại transaction này, để khi người dùng ấn back thì vẫn nhớ lại UI trước đó. Nếu không có nó, thì ta vừa thay đổi 1 chiều, tức khi ấn back thì sẽ thoát ra tới fragment trước fragment vừa chuyển hoặc thoát ra hẳn activity luôn chứ không quay về fragment trước. Ví dụ:
    **Không dùng `addToBackStack()`**:
    ```bash
    FragmentTransaction tx = fragmentManager.beginTransaction();
    tx.replace(R.id.container, new FragmentB());
    tx.commit();
    ```
    Màn hình đang ở FragmentA.
    replace sang FragmentB.
    Bấm Back → Activity đóng (vì Android không nhớ đã từng ở FragmentA).

    **Có dùng addToBackStack()**
    ```bash
    FragmentTransaction tx = fragmentManager.beginTransaction();
    tx.replace(R.id.container, new FragmentB());
    tx.addToBackStack(null);
    tx.commit();
    ```
    Màn hình đang ở FragmentA.
    replace sang FragmentB.
    Bấm Back → Quay lại FragmentA (vì transaction đã được lưu trong back stack).

- Chính vì việc nếu không dùng `addToBackStack()` ta sẽ không thể nào quay lại các transaction trước nên cần tránh xen kẽ các transaction ảnh hưởng đến backstack với các transaction không ảnh hưởng đến backstack.

### c. FragmentTransaction:
- Trong runtime, `FragmentManager` quản lý các thao tác với backstack như thêm, xóa, thay thế liên quan tới các fragment và mỗi lần thực hiện 1 thao tác thay đổi đó gọi là 1 `transaction` và quản lý các `transaction` này thông qua `FragmentTransaction`.
- Trong 1 `transaction` ta có thể sử dụng các phương thức như: `add()` hoặc `replace()`.
- Các phương thức trong `FragmentTransaction`:

| **Phương thức** | **Chức năng**                                                              | **Đặc điểm**                                                                                                      |
| --------------- | -------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------- |
| `add()`         | Thêm một fragment mới vào `FragmentManager`.                               | Fragment mới sẽ được thêm vào container, fragment cũ (nếu có) vẫn tồn tại song song trừ khi bạn remove/detach nó. |
| `attach()`      | Gắn lại một fragment đã bị `detach()` trước đó vào UI.                     | Fragment được hiển thị lại, state trước đó vẫn được giữ nguyên (vì detach không xóa khỏi `FragmentManager`).      |
| `commit()`      | Xác nhận và thực thi các thao tác đã khai báo trong `FragmentTransaction`. | Sau khi gọi, transaction sẽ được áp dụng; có thể dùng `commitNow()` để thực hiện ngay lập tức.                    |
| `detach()`      | Gỡ fragment khỏi UI nhưng vẫn giữ nó trong `FragmentManager`.              | View của fragment bị destroy, nhưng state vẫn lưu để có thể `attach()` lại nhanh chóng.                           |
| `remove()`      | Xóa fragment khỏi `FragmentManager`.                                       | State và view của fragment bị hủy hoàn toàn; nếu muốn hiển thị lại phải tạo mới.                                  |
| `replace()`     | Xóa tất cả fragment trong container và thêm fragment mới vào.              | Thực chất là `remove()` tất cả fragment trong container rồi `add()` fragment mới.                                 |

- Ví dụ:
```kotlin
fun navigateToSignUp() {
    supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, SignUpFragment())
        .addToBackStack(null)
        .commit()
}
```
hoặc dùng extension function của **KTX** (Kotlin Extensions)
```kotlin
fun navigateToSignUp() {
    supportFragmentManager.commit {
        replace<SignUpFragment>(R.id.fragment_container)
        setReorderingAllowed(true)
        addToBackStack(null)
    }
}
```
---

## II. Fragment Lifecycle
![](https://media.geeksforgeeks.org/wp-content/uploads/20230120013956/AndroidFragmentLifecycle.png)

- Các callback:

| Callback        | Mô tả                                                                                                                                                                                                                                                                                                                                                          |
| --------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| onAttach()      | Phương thức đầu tiên được gọi khi đoạn mã đã được liên kết với hoạt động. Phương thức này chỉ thực thi một lần trong suốt vòng đời của đoạn mã.Khi chúng ta gắn đoạn mã (con) vào hoạt động chính (cha), đoạn mã sẽ gọi trước và sau đó không gọi phương thức này nữa (giống như khi bạn chạy ứng dụng, đóng và mở lại), phương thức này chỉ được gọi một lần. |
| onCreate()      | Phương pháp này khởi tạo đoạn mã bằng cách thêm tất cả các thuộc tính và thành phần cần thiết.                                                                                                                                                                                                                                                                 |
| onCreateView()  | Hệ thống gọi phương thức này để tạo giao diện người dùng cho đoạn mã. Gốc của bố cục đoạn mã được trả về dưới dạng thành phần View bởi phương thức này để vẽ giao diện người dùng. Bạn nên mở rộng bố cục của mình trong `onCreateView` nhưng không nên khởi tạo các chế độ xem khác bằng findViewById trong `onCreateView`.                                   |
| onViewCreated() | Điều này cho biết hoạt động mà đoạn mã đó tồn tại đã được tạo. Xem hệ thống phân cấp của đoạn mã cũng được khởi tạo trước khi gọi hàm này.                                                                                                                                                                                                                     |
| onStart()       | Hệ thống sử dụng phương pháp này để làm cho đoạn mã hiển thị trên thiết bị của người dùng.                                                                                                                                                                                                                                                                     |
| onResume()      | Phương pháp này được gọi để làm cho phần hiển thị có tính tương tác.                                                                                                                                                                                                                                                                                           |
| onPause()       | Điều này cho biết người dùng đang rời khỏi phân đoạn. Hệ thống gọi phương thức này để xác nhận những thay đổi đã thực hiện trên phân đoạn.                                                                                                                                                                                                                     |
| onStop()        | Phương pháp chấm dứt hoạt động và khả năng hiển thị của đoạn mã trên màn hình của người dùng.                                                                                                                                                                                                                                                                  |
| onDestroyView() | Hệ thống gọi phương thức này để dọn dẹp tất cả các loại tài nguyên cũng như xem hệ thống phân cấp liên quan đến phân đoạn. Phương thức này sẽ gọi khi bạn có thể gắn phân đoạn mới và hủy phân đoạn hiện có.                                                                                                                                                   |
| onDestroy()     | Nó được gọi để thực hiện việc dọn dẹp cuối cùng trạng thái của đoạn mã và vòng đời của nó.                                                                                                                                                                                                                                                                     |
| onDetach()      | Hệ thống thực thi phương thức này để tách phân đoạn khỏi hoạt động máy chủ của nó. Phương thức này sẽ được gọi khi phân đoạn của bạn bị hủy (ứng dụng bị sập hoặc gắn phân đoạn mới vào phân đoạn hiện có).                                                                                                                                                    |

- Các trạng thái của `fragment lifecycle` được biểu thị trong enum `Lifecycle.state`:
  - ***`INITIALIZED`***: Fragment đã được tạo **instance** (object được khởi tạo bằng constructor) nhưng chưa được gắn (attached) vào **LifecycleOwner** và chưa nhận callback `onCreate()`.
  - ***`CREATED`***: Fragment đã được attach vào **LifecycleOwner** (thường là Activity), `onCreate()` đã chạy nhưng View có thể chưa được tạo.
  - ***`STARTED`***: Fragment đang hiển thị trên màn hình hoặc ít nhất là có thể nhìn thấy một phần, nhưng chưa tương tác trực tiếp (chưa ở trạng thái foreground hoàn toàn).
  - ***`RESUMED`***: Fragment đang hiển thị và hoạt động ở foreground, sẵn sàng nhận input người dùng.
  - ***`DESTROYED`***: Fragment đã bị gỡ khỏi Activity hoặc bị hủy hoàn toàn.
*`LifecycleOwner` là 1 interface dùng để đại điện cho 1 đối tượng có vòng đời (lifecycle) và cho phép các thành phần khác quan sát (observe) sự thay đổi của vòng đời đó*
![](https://1.bp.blogspot.com/-aG6wRFugP8Q/XuDcUgfyz2I/AAAAAAAAEfs/FdMOlCFgUs8_JDsLYPy2gRYJX3HOLpcbgCK4BGAsYHg/d/1.png  )

- Activity lifecycle & Fragment lifecycle:
![](https://miro.medium.com/v2/resize:fit:1100/format:webp/1*K65nu8Fz09bBj-IOLvDPgA.png)
---

## III. Giao tiếp Fragment với Fragment, Activity với Fragment
### a. Truyền dữ liệu vào Fragment:
- Truyền các `arguments` vào Fragment.
#### 1. Dùng `newInstance()` + `arguments` (Bundle):
- Khởi tạo 1 object của class rồi truyền các dữ liệu vào, gộp thành bundle và tạo mới 1 fragment
```kotlin
class MyFragment : Fragment() {
    companion object {
        private const val ARG_TITLE = "arg_title"
        fun newInstance(title: String) = MyFragment().apply {
            arguments = Bundle().apply { putString(ARG_TITLE, title) }
        }
    }

    private var title: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(ARG_TITLE)
    }
}
```
Khi cần chuyển fragment:
```kotlin
parentFragmentManager.beginTransaction()
    .replace(R.id.container, MyFragment.newInstance("Hello"))
    .commit()
```
- Ưu điểm: Khi xoay màn hình thì vẫn giữ nguyên được dữ liệu
- Nhược điểm: Chỉ truyền dữ liệu lúc tạo, muốn cập nhật dữ liệu phải tạo lại.

#### 2. Tạo `Bundle` trực tiếp bằng `apply`/`bundleOf`:
Dùng KTX
```kotlin
val bundle = bundleOf("score" to 100, "name" to "Hai")
parentFragmentManager.commit {
  setReorderingAllowed(true)  
  add<MyFragment>(R.id.fragment_container_view, args = bundle)
}
```

```kotlin
val fragment = MyFragment()
val args = Bundle()
args.putInt("score", 100)
args.putString("name", "Hai")
fragment.arguments = args

val transaction = parentFragmentManager.beginTransaction()
transaction.setReorderingAllowed(true)
transaction.add(R.id.fragment_container_view, fragment)
transaction.commit()
```
Lấy dữ liệu ra trong Fragment
```kotlin
val score = requireArguments().getInt("score", 0)
val name = requireArguments().getString("name", "")
```

### b. Fragment với Fragment:
- Sử dụng:

| Hàm                                                        | Vai trò                                                  |
| ---------------------------------------------------------- | -------------------------------------------------------- |
| `setFragmentResult(key, bundle)`                           | **Gửi** dữ liệu (Bundle) kèm với một key định danh.      |
| `setFragmentResultListener(key, lifecycleOwner, listener)` | **Nhận** dữ liệu khi có bên khác gửi với key trùng khớp. |

```kotlin
//Fragment A chuyển sang B và lắng nghe kết quả từ B
binding = FragmentABinding.inflate(inflater, container, false)

parentFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) { key, bundle ->
    val score = bundle.getInt("score")
    val name = bundle.getString("name")
    binding.textView.text = "Name: $name - Score: $score"
}
```

```kotlin
//Fragment B thực hiện công việc và chuyển dữ liệu về A và quay lại về fragment A

binding.buttonSend.setOnClickListener {
    val result = Bundle().apply {
        putInt("score", 100)
        putString("name", "Hai")
    }
    parentFragmentManager.setFragmentResult("requestKey", result)
}
```
- Hoạt động của 2 code trên:
  - A đăng ký để chuyển sang B
  - B thực hiện công việc và trả về dữ liệu cho A
  - A nhận dữ liệu khi B gửi về và quay lại A
### c. Activity với Fragment:
#### 1. Chuyển từ Fragment -> Activity:
```kotlin
// MainActivity.kt
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lắng nghe dữ liệu do Fragment gửi về
        supportFragmentManager.setFragmentResultListener("requestKey", this) { requestKey, bundle ->
            val result = bundle.getString("bundleKey")
            // Xử lý dữ liệu nhận được
        }
    }
}
```

Trong đó:

- `setFragmentResultListener("requestKey", this, listener)` đăng ký để lắng nghe khi có Fragment nào đó gửi kết quả với key `"requestKey"`.
- `bundle` chứa dữ liệu từ Fragment gửi.
- Hoạt động tương tự như callback, nhưng không cần tạo interface.

- Fragment gửi dữ liệu sẽ làm như sau:
```kotlin
supportFragmentManager.setFragmentResult(
    "requestKey",
    Bundle().apply { putString("bundleKey", "Hello from Fragment") }
)
```

#### 2. Chuyển từ Activity -> Fragment:
```kotlin
//MainActivity.kt
supportFragmentManager.setFragmentResult(
    "request",
    Bundle().apply { putInt("key", 9) }
)
```

```kotlin
//Fragment nhận dữ liệu
parentFragmentManager.setFragmentResultListener("request", this) { requestKey, bundle ->
    val value = bundle.getInt("key")
    // Xử lý dữ liệu nhận từ MainActivity
}
```
