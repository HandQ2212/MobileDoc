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
        class MyViewHolder(val binding: ExamCardBinding) : RecyclerView.ViewHolder(binding.root)
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

### c. Tính tái sử dụng:


## II. Adapter và ListAdapter:

## III. RecyclerView Multiple View Type:
