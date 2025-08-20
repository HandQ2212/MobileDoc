# Buổi 10: Recycle View

## I. Tổng quan RecyclerView
### a. Định nghĩa & Các bước tạo 1 RecyclerView:
- **RecyclerView** là **1 ViewGroup** được kế thừa từ GridView và ListView. 
- **RecyclerView** được tạo ra để cho phép xây dựng bất kỳ **danh sách** nào với bố cục XML dưới dạng 1 mục **có thể tùy chỉnh đáng kể**, đồng thời *cải thiện hiệu quả* của ListView và GridView. Cải tiến ở việc **recycling** các view mà người dùng không thấy được.
- Để triển khai 1 **RecyclerView** cơ bản, cần xây dựng 3 thành phần phụ:
  - **The Card Layout**: Xây dựng bố cục XML cho 1 mục trong danh sách (danh sách sẽ bao gồm nhiều mục này nhưng mà nội dung trong mỗi card sẽ khác nhau tùy vào nội dung được chuyển vào)
  - **ViewHolder**: ViewHolder là một lớp Java lưu trữ tham chiếu đến các chế độ xem bố cục thẻ phải được sửa đổi động trong quá trình thực thi chương trình bằng danh sách dữ liệu lấy từ cơ sở dữ liệu trực tuyến hoặc được thêm vào theo một cách nào đó.
  - **The Data Class**: Data class là một lớp Java tùy chỉnh đóng vai trò là một cấu trúc để lưu trữ thông tin cho mọi mục của **RecyclerView**.
  
  ![](https://tutorials.eu/wp-content/uploads/2020/07/RecycleViewApp.png)
- Các bước tạo 1 RecyclerView:
  - Bước 1: Tạo XML (Tạo Card & Layout chứa recyclerView):
    - Tạo Card (`exam_card.xml`):
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <RelativeLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="105dp">

        <TextView
            android:layout_width="200dp"
            android:id="@+id/examName"
            android:textSize="16sp"
            android:layout_marginStart="20dp"
            android:text="First Exam"
            android:textColor="@color/black"
            android:layout_marginEnd="20dp"
            android:maxLines="1"
            android:layout_marginTop="15dp"
            android:layout_height="wrap_content"/>

        <ImageView
            android:id="@+id/examPic"
            android:layout_width="20dp"
            android:layout_height="20dp"
            android:layout_below="@+id/examName"
            android:layout_marginStart="20dp"
            android:layout_marginTop="7dp"
            app:srcCompat="@drawable/schedule"/>

        <TextView
            android:id="@+id/examDate"
            android:layout_toEndOf="@+id/examPic"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_below="@+id/examName"
            android:layout_marginTop="5dp"
            android:layout_marginEnd="20dp"
            android:layout_marginStart="10dp"
            android:gravity="center"
            android:text="May 23, 2015"
            android:textSize="16sp"/>

        <ImageView
            android:id="@+id/examPic2"
            android:layout_width="20dp"
            android:layout_height="20dp"
            android:layout_below="@+id/examDate"
            android:layout_marginStart="20dp"
            android:layout_marginTop="7dp"
            app:srcCompat="@drawable/school"/>

        <TextView
            android:id="@+id/examMessage"
            android:layout_toEndOf="@+id/examPic2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_below="@+id/examDate"
            android:layout_marginEnd="20dp"
            android:layout_marginTop="5dp"
            android:layout_marginStart="10dp"
            android:gravity="center"
            android:text="Best Of Luck"
            android:textSize="16sp"/>

        <TextView
            android:id="@+id/border2"
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:layout_marginStart="15dp"
            android:layout_marginEnd="15dp"
            android:layout_alignParentBottom="true"
            android:background="#808080"/>

    </RelativeLayout>
    ```
    - Tạo Layout chứa recyclerView (activity_main.xml):
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <ScrollView
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/app_background"
        tools:context=".MainActivity" >

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical" >

            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="Exam Schedule"
                android:textAlignment="center"
                android:background="@android:color/darker_gray"
                android:paddingTop="54sp"
                android:paddingBottom="20sp"
                android:textSize="24sp"/>

            <androidx.recyclerview.widget.RecyclerView
                android:nestedScrollingEnabled="false"
                android:id="@+id/recyclerView"
                android:layout_width="match_parent"
                android:overScrollMode="never"
                android:layout_height="wrap_content"/>

        </LinearLayout>
    </ScrollView>
    ```
  - Bước 2: Xác định các thành phần sẽ được chèn vào trong card:
    - Tạo 1 file `ExamItem.kt` (vì card đang là `exam_card.xml`). Class này chứa các thuộc tính trong card:

    ```kotlin
    package com.gfg.recyclerview_kotlin

    data class ExamItem(
        val name: String,
        val date: String,
        val message: String,
        val image1: Int,
        val image2: Int
    )
    ```

  - Bước 3: Xác định Apdapter:
    - `Adapter`: Là code chính chịu trách nhiệm cho RecyclerView. Nó chứa tất cả các **phương thức** quan trọng liên quan đến việc triển khai **RecyclerView**. Các phương thức cơ bản như:
      - `onCreateViewHolder` : xử lý việc mở rộng bố cục thẻ thành một mục cho **RecyclerView**.
      - `onBindViewHolder` : xử lý việc thiết lập các dữ liệu và phương pháp khác nhau liên quan đến việc nhấp vào các mục cụ thể của **RecyclerView**.
      - `getItemCount` : Trả về độ dài của RecyclerView.
      - `onAttachedToRecyclerView` : dùng để gắn **Adapter** vào RecyclerView.

    `MyAdapter.kt`
    ```kotlin
    package com.gfg.example_recyclerview

    import android.view.LayoutInflater
    import android.view.ViewGroup
    import androidx.recyclerview.widget.RecyclerView
    import com.gfg.recyclerview_kotlin.ExamItem
    import com.gfg.recyclerview_kotlin.databinding.ExamCardBinding

    class MyAdapter(private val examList: List<ExamItem>) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val binding = ExamCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return MyViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val examItem = examList[position]

            holder.binding.examName.text = examItem.name
            holder.binding.examDate.text = examItem.date
            holder.binding.examMessage.text = examItem.message

            holder.binding.examPic.setImageResource(examItem.image1)
            holder.binding.examPic2.setImageResource(examItem.image2)
        }

        override fun getItemCount(): Int {
            return examList.size
        }

        // ViewHolder class dùng binding
        inner class MyViewHolder(val binding: ExamCardBinding) : RecyclerView.ViewHolder(binding.root)
    }

    ```
  - Bước 4: Thực hiện truyền dữ liệu đối với Activity/Fragment chứa RecyclerView này (làm tại `onCreate()`):    
    `MyActivity.kt`
    ```kotlin
    package com.gfg.recyclerview_kotlin

    import android.os.Bundle
    import androidx.appcompat.app.AppCompatActivity
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.gfg.example_recyclerview.MyAdapter
    import com.gfg.recyclerview_kotlin.databinding.ActivityMainBinding

    class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Sample data
            val examList: MutableList<ExamItem> = ArrayList()
            examList.add(
                ExamItem(
                    "Math Exam",
                    "May 23, 2015",
                    "Best of Luck",
                    R.drawable.schedule,
                    R.drawable.school
                )
            )
            examList.add(
                ExamItem(
                    "Science Exam",
                    "June 10, 2015",
                    "Do Well",
                    R.drawable.schedule,
                    R.drawable.school
                )
            )
            examList.add(
                ExamItem(
                    "History Exam",
                    "July 15, 2015",
                    "All the Best",
                    R.drawable.schedule,
                    R.drawable.school
                )
            )
            examList.add(
                ExamItem(
                    "English Exam",
                    "August 1, 2015",
                    "Stay Confident",
                    R.drawable.schedule,
                    R.drawable.school
                )
            )

            // Set LayoutManager
            // RecyclerView dạng list dọc
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            // RecyclerView dạng list ngang
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            // Set Adapter
            val adapter = MyAdapter(examList)
            binding.recyclerView.adapter = adapter
        }
    }
    ```
### b. Ưu điểm:
- Tái sử dụng View hiệu quả thông qua ViewHolder, nhờ đó giảm số lần gọi `binding`/`findViewById` nhiều lần, giảm lag, tiết kiệm bộ nhớ.
- Linh hoạt trong bố cục, có thể dễ dàng thay thế các bố cục mà không cần viết lại adapter
- Hỗ trợ các Animation, khi thêm/sửa/xóa các thành phần trong danh sách, nó sẽ tự động hỗ trợ chuyển đổi hiệu ứng mượt mà thông qua `ItemAnimation`
- Dễ dàng thêm khoảng cách giữa các item, vẽ đường phân cách, hoặc hỗ trợ thao tác kéo-thả, vuốt để xóa. (Thông qua `ItemDecoration`, `ItemTouchHelper`)
- **RecyclerView** chỉ tạo ra số lượng view vừa đủ để hiển thị trên màn hình, cuộn tới đâu thì tái sử dụng view cũ, giúp tiết kiệm RAM và tăng hiệu năng.
- Dễ dàng mở rộng, chia nhỏ code, hỗ trợ MVVM/MVP hoặc kiến trúc hiện đại của Android.
=> Tiết kiệm bộ nhớ, animation linh hoạt, thích ứng (ko nhớ từ) với các cấu trúc mới.
### c. Tính tái sử dụng:
- Khi item ra khỏi màn hình → thay vì hủy View, **RecyclerView** sẽ đưa nó vào một **Recycle Pool** (bộ nhớ đệm View).

- Khi cần hiển thị item mới (ví dụ cuộn xuống):

- **RecyclerView** tái sử dụng View đã có trong Pool.

- Chỉ thay đổi dữ liệu hiển thị (bind data) thay vì tạo mới toàn bộ View.

- Điều này được hỗ trợ bởi ViewHolder – nơi giữ sẵn tham chiếu tới các View con trong layout để không phải gọi `findViewById`/`binding` nhiều lần.

- Ví dụ:
  - Giả sử có danh sách 10.000 phần tử, nhưng màn hình chỉ hiển thị 10 item cùng lúc. **RecyclerView** chỉ tạo khoảng 10–12 View, rồi liên tục tái sử dụng chúng khi người dùng cuộn.
  - Tức là **RecyclerView** không tạo View mới cho từng item, mà tái sử dụng View cũ → chỉ thay dữ liệu → vừa tiết kiệm bộ nhớ, vừa tăng hiệu năng.

### d. Các thành phần của RecyclerView:
<details>
  <summary>LayoutManager</summary>
  <p>LayoutManager chịu trách nhiệm xác định cách sắp xếp các item trong RecyclerView.</p>
  <ul>
    <li><b>LinearLayoutManager</b>: hiển thị theo chiều dọc/ngang.</li>
    <li><b>GridLayoutManager</b>: hiển thị theo dạng lưới.</li>
    <li><b>StaggeredGridLayoutManager</b>: dạng lưới những mà các item trong danh sách có thể có chiều cao không bằng nhau.</li>
  </ul>
</details>

<details>
    <summary>Adapter</summary>
    <ul>
    - Là nơi nhận vào dữ liệu để hiển thị lên màn hình người dùng    </ul>
    <ul>- Là 1 class kế thừa từ <code>RecyclerView.Adapter</code></ul>
</details>

<details>
    <summary>ViewHolder</summary>
    <ul>
    - Là 1 helper class, nó sẽ giúp ta vẽ UI cho từng item mà chúng ta đẩy lên màn hình. Tất cả các tác vụ binding của views sẽ diễn ra ở đây. 
    </ul>
    <ul>
    - Nó là 1 subclass của <code>RecyclerView.ViewHolder</code>
    </ul>
</details>

<details>
    <summary>ItemAnimator</summary>
    <ul>
    - <code>RecyclerView.ItemAnimator</code> là 1 class dùng để thêm hiệu ứng khi thay đổi các phần tử trong <b>RecyclerView</b>. Khi thêm/xóa phần tử trong RecyclerView, nó sẽ xử lý các hiệu ứng chuyển động của các phần tử này, giúp tạo ra các animation hợp mắt người dùng hơn.
    </ul>
    <ul>
    - <code>DefaultItemAnimator</code>: Là ItemAnimator mặc định khi không cung cấp ItemAnimator nào. Nó cung cấp các hiệu ứng mặc định khi thêm/xóa và update các phần tử
    </ul>
</details>


## II. Adapter và ListAdapter:
### a. Adapter:
- `Adapter`: Là code chính chịu trách nhiệm cho RecyclerView. Nó chứa tất cả các **phương thức** quan trọng liên quan đến việc triển khai **RecyclerView**. Các phương thức cơ bản như:
      - `onCreateViewHolder` : xử lý việc mở rộng bố cục thẻ thành một mục cho **RecyclerView**.
      - `onBindViewHolder` : xử lý việc thiết lập các dữ liệu và phương pháp khác nhau liên quan đến việc nhấp vào các mục cụ thể của **RecyclerView**.
      - `getItemCount` : Trả về độ dài của RecyclerView.
      - `onAttachedToRecyclerView` : dùng để gắn **Adapter** vào RecyclerView.
### b. ListAdapter:

- `ListAdapter` là một lớp con của `RecyclerView.Adapter`, được giới thiệu để **giảm bớt boilerplate code** *(những đoạn code lặp đi lặp lại nhưng mà cần thiết)* khi làm việc với RecyclerView.
- Nó được thiết kế đặc biệt để **làm việc với danh sách dữ liệu có thể thay đổi**, kết hợp với `DiffUtil` để tự động tính toán sự khác biệt giữa danh sách cũ và danh sách mới, từ đó cập nhật RecyclerView **mượt và hiệu quả** hơn.

- **Các đặc điểm chính:**
  - **Kế thừa từ `RecyclerView.Adapter`**: Tất cả các phương thức cơ bản (`onCreateViewHolder`, `onBindViewHolder`, …) vẫn hoạt động tương tự Adapter.

  - **Tích hợp sẵn `DiffUtil`**
    - Không cần tự viết thủ công logic cập nhật dữ liệu.
    - Chỉ cần implement `DiffUtil.ItemCallback<T>` để xác định cách so sánh hai item.

- **Phương thức quan trọng**
    - `submitList(List<T> list)` : thay cho việc gọi `notifyDataSetChanged()`. Nó sẽ tính toán sự khác biệt và chỉ cập nhật những item thực sự thay đổi.
    - `getCurrentList()` : trả về danh sách hiện tại mà adapter đang giữ.
- **Hiệu suất cao hơn Adapter thường**: Khi dữ liệu thay đổi, `ListAdapter` chỉ update những item thay đổi → không bị giật lag do cập nhật toàn bộ RecyclerView.

- So sánh:
  - `Adapter`: phải tự viết logic cập nhật dữ liệu (`notifyDataSetChanged`, `notifyItemChanged`, …).
  - `ListAdapter`: tích hợp sẵn `DiffUtil`, chỉ cần gọi `submitList()`, phần còn lại được xử lý tự động, nhanh và gọn hơn.

## III. RecyclerView Multiple View Type:
- Là một RecyclerView có thể hiển thị nhiều loại item layout khác nhau thay vì chỉ một loại duy nhất.
- Bình thường khi dùng RecyclerView, ta có 1 Adapter với 1 ViewHolder và 1 layout item. Nhưng trong nhiều trường hợp, dữ liệu hiển thị không đồng nhất (ví dụ: tin nhắn chat có tin gửi và tin nhận, trang thương mại điện tử có banner, sản phẩm, quảng cáo,…). Khi đó ta cần Multiple View Types.
- Cách hoạt động:
  - RecyclerView quyết định loại layout nào sẽ được hiển thị thông qua phương thức:
  ```kotlin
  override fun getItemViewType(int position) {

  }
  ```
  - Các phương thức:
    - `getItemViewType(position)`: dựa vào dữ liệu ở position để trả về một viewType (ví dụ: 0 cho banner, 1 cho sản phẩm).
    - `onCreateViewHolder(ViewGroup parent, int viewType)`: dựa vào viewType để inflate đúng layout.
    - `onBindViewHolder(holder, position)`: gán dữ liệu tương ứng với loại layout đó.
- Ví dụ về trạng thái của tin nhắn:


Model `Message`
```kotlin
package com.example.tmp

enum class MessageStatus {
    SENDING,
    SENT,
    RECEIVED,
    READ
}

data class Message(
    val text: String,
    val isSent: Boolean,          // true = mình gửi, false = mình nhận
    val status: MessageStatus? = null  // chỉ dùng cho tin nhắn gửi đi
)
```

Adapter `ChatAdapter`
```kotlin
package com.example.tmp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmp.databinding.ItemReceivedBinding
import com.example.tmp.databinding.ItemSentBinding

class ChatAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_SENT = 0
        private const val TYPE_RECEIVED = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isSent) TYPE_SENT else TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_SENT -> SentViewHolder(
                ItemSentBinding.inflate(inflater, parent, false)
            )
            TYPE_RECEIVED -> ReceivedViewHolder(
                ItemReceivedBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalArgumentException("Invalid viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messages[position]
        when (holder) {
            is SentViewHolder -> holder.bind(msg)
            is ReceivedViewHolder -> holder.bind(msg)
        }
    }

    override fun getItemCount(): Int = messages.size

    inner class SentViewHolder(private val binding: ItemSentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(msg: Message) {
            binding.tvMessageSent.text = msg.text
            binding.tvStatus.text = when (msg.status) {
                MessageStatus.SENDING -> "Đang gửi..."
                MessageStatus.SENT -> "Đã gửi"
                MessageStatus.RECEIVED -> "Đã nhận"
                MessageStatus.READ -> "Đã đọc"
                else -> ""
            }
        }
    }

    inner class ReceivedViewHolder(private val binding: ItemReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(msg: Message) {
            binding.tvMessageReceived.text = msg.text
        }
    }
}
```
`MainActivity`
```kotlin
package com.example.tmp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tmp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()

        val messages = listOf(
            Message("Xin chào!", true, MessageStatus.SENDING),
            Message("Hello!", false),
            Message("Bạn khỏe không?", true, MessageStatus.SENT),
            Message("Mình khỏe, cảm ơn.", false),
            Message("Ok, hẹn gặp lại.", true, MessageStatus.READ)
        )

        setUpRecyclerView(messages)
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setUpRecyclerView(messages: List<Message>) {
        chatAdapter = ChatAdapter(messages)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity
            ).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
        }
    }
}
```

`item_sent.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="8dp"
    android:gravity="end"
    android:orientation="vertical">

    <!-- Trạng thái tin nhắn -->
    <TextView
        android:id="@+id/tvMessageSent"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="4dp"
        android:background="@android:color/holo_blue_dark"
        android:padding="10dp"
        android:text="Tin nhắn gửi đi"
        android:textColor="#212121" />

    <TextView
        android:id="@+id/tvStatus"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="12sp"
        android:textColor="@android:color/darker_gray"
        android:layout_marginTop="2dp"
        android:text="Đang gửi..." />
</LinearLayout>
```
`item_received.xml`
```kotlin
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="8dp"
    android:gravity="start"
    android:orientation="vertical">

    <TextView
        android:id="@+id/tvMessageReceived"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@android:color/white"
        android:textColor="@android:color/black"
        android:padding="10dp"
        android:text="Tin nhắn nhận được"
        android:layout_margin="4dp"/>
</LinearLayout>
```
`ActivityMain`
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</LinearLayout>
```