# Buổi 6: Android Overview


## I. Android Overview
### a. Hệ điều hành android và Android Studio
#### 1. Android OS:
- Android là hệ điều hành dựa trên Linux được thiết kế dành cho mobile device có màn hình cảm ứng như điện thoại thông minh và máy tính bảng. Android có mã nguồn mở.
- Ngôn ngữ: Được viết bằng Java, Kotlin (UI), C (nhân), C++ và 1 số ngôn ngữ khác
- Android được phát triển bởi Android Inc. với sự hỗ trợ tài chính từ Google và sau này được chính Google mua lại vào năm 2005.
#### 2. Android Studio:
- Android Studio là IDE chính thức dành cho phát triển nền tảng trên Android.
- Được ra mắt vào ngày 15/03/2013 tại hội nghị Google I/O. Phiên bản ổn định lần đầu được ra mắt vào 12/2024 (p.bản 1.0).
- Được dựa trên phần mềm **Intellij IDEA** của JetBrains. Hỗ trợ các hệ điều hành Windows, Mac OS X và Linux
- Các tính năng nổi bật:
  - Trình giả lập Android (Android Emulator)
  - Build hệ thống với Gradle
  - Công cụ kiểm thử và gỡ lỗi: Logcat, Debugger, Profiler
### b. Android folder project structure - Cấu trúc thư mục dự án Android:
![](AndroidStructure.png). Cấu hình cơ bản của file:
#### 1. manifests - Thư mục kê khai:
- Chứa file `AndroidManifests.xml`
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.myapp">

    <!-- Khai báo quyền sử dụng -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.CAMERA" />

    <!-- Yêu cầu phiên bản SDK -->
    <uses-sdk
        android:minSdkVersion="21"
        android:targetSdkVersion="33" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.MyApp">

        <!-- Activity khởi động khi mở app -->
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Khai báo thêm activity nếu có -->
        <activity android:name=".SecondActivity" />

        <!-- Các service, receiver... nếu có -->
    </application>
</manifest>
```
- Mục đích của manifest:

| Vai trò| Giải thích|
|----| -----|
| **Khai báo app**                     | Định danh tên gói (`package`) và cấu hình ứng dụng.|
| **Khai báo thành phần (components)** | Gồm: `Activity`, `Service`, `BroadcastReceiver`, `ContentProvider`.               |
| **Khai báo quyền (permissions)**     | Quyền như internet, camera, GPS…                                                  |
| **Cấu hình metadata**                | Dành cho cấu hình theme, launcher, thư viện bên ngoài…                            |
| **Chỉ định Intent Filters**          | Xác định các hoạt động nào được khởi chạy từ ngoài (launcher, chia sẻ, mở link…). |

#### 2. Thư mục java hoặc kotlin+java:
- Là thư mục chứa các tệp mã kotlin/java được tạo trong quá trình phát triển ứng dụng. 
- Tự động có 1 file MainActivity.java/kt tự động tạo trong package `com.example.<project name>`

#### 3. Resource Folder (res) - Thư mục tài nguyên:
Thư mục tài nguyên là thư mục quan trọng nhất vì nó chứa tất cả các code không phải mã như hình ảnh, bố cục XML và chuỗi giao diện người dùng cho ứng dụng Android.
- `res/drawable/`: Thư mục dùng để chứa các loại hình ảnh khác nhau để phát triển app. 
- `res/layout/`: Thư mục layout chứa tất cả các tệp layout XML mà chúng ta đã sử dụng để định nghĩa giao diện người dùng cho ứng dụng.
- `res/mipmap/`: Dùng để chứa các biểu tượng ứng dụng **(launcher icons)** với nhiều độ phân giải, giúp Android hiển thị phù hợp trên mọi loại màn hình một cách tối ưu.
- `res/values/`: Thư mục Values chứa một số tệp XML như chuỗi, kích thước, màu sắc và định nghĩa kiểu. Một trong những tệp quan trọng nhất là tệp strings.xml chứa các tài nguyên.


#### 4. Gradle Scripts:
Gradle là hệ thống xây dựng tự động và chứa một số tệp được sử dụng để xác định cấu hình xây dựng có thể áp dụng cho tất cả các module trong ứng dụng.
- Trong `build.gradle (Project)` có các buildscripts
- Trong `build.gradle (Module)`, plugins và implementation được sử dụng để xây dựng các cấu hình có thể áp dụng cho tất cả các module ứng dụng.

### c. XML
- **XML** là từ viết tắt của từ **Extensible Markup Language** là ngôn ngữ đánh dấu mở rộng được thiết kế để lưu trữ và truyền tải dữ liệu một cách có cấu trúc. Tác dụng chính của XML là đơn giản hóa việc chia sẻ dữ liệu giữa các nền tảng và các hệ thống được kết nối thông qua mạng Internet.
- Trong Android, XML được sử dụng để mô tả giao diện người dùng (UI) và các tài nguyên. Thay vì viết bằng Java/Kotlin thuần thì Android khuyến khích nên thiết kế giao diện bằng XML tách biệt để dễ quản lý, dễ bảo trì và hỗ trợ nhiều loại thiết bị.
- Các file XML thường được dùng ở:

| Nơi sử dụng     | Tác dụng                              |
| --------------- | ------------------------------------- |
| `layout/`       | Giao diện màn hình                    |
| `values/`       | Văn bản, màu sắc, kích thước, style   |
| `drawable/`     | Hình vẽ bằng XML (shape, selector...) |
| `manifest/`     | Cấu hình tổng thể ứng dụng            |
| `menu/`         | Khai báo menu                         |
| `anim/`         | Hiệu ứng animation                    |

### d. Tool cơ bản có trong Android studio
- Debugger:
  - Dùng để đặt breakpoint, xem giá trị biến, luồng chạy.
  - Có thể tạm dừng, xem stack trace, bước qua từng dòng code.
- Logcat: 
  - Hiển thị log (ghi chú chạy) của ứng dụng đang chạy trên máy thật hoặc giả lập.
  - Dùng để: Xem Log.d, Log.e... Gỡ lỗi (debug). Tìm crash (Exception).
- Layout inspector: 
  - Dùng để kiểm tra giao diện của người dùng
  - Có thể so sánh bố cục ứng dụng với mô hình thiết kế, hiển thị chế độ xem phóng to hoặc 3D của ứng dụng và kiểm tra chi tiết bố cục tại thời gian chạy.
- Layout Editor: Dùng để thiết kế UI (giao diện) cho các màn hình Activity hoặc Fragment. Có 3 chế độ: Kéo thả, viết XML hoặc cả 2. 
- Emulator: Mô phỏng một thiết bị Android trên máy tính. Có thể tùy chỉnh cấu hình: màn hình, RAM, API level...
- Android Virtual Device (AVD): Đây là trình quản lý các Emulator. Chọn phiên bản Android, độ phân giải, CPU, RAM...
### e. View cơ bản trong Android Studio
- Những gì nhìn thấy trên màn hình thiết bị Android được gọi là `View`. `View` được vẽ trên thiết bị Android với 1 hình chữ nhật.
- Các View cơ bản và thường xuyên sử dụng trong Android như: `TextView`, `EditText`, `ImageView`, `Button`, `CheckBox`, `RadioButton`. Các `View` này đều được kế thừa từ class `View`.
- Cách tạo `View` trong Android:
  - Dùng XML:
  ```xml
  <TextView
        android:id="@+id/tv_Favorites"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Yêu Thích"/>
  ```
  - Dùng Java:
  ```java
  TextView tvFavorites = new TextView(this);
  tvFavorites.setText("Yêu thích");
  ```

- Các thuộc tính của View:
<details>

<summary><strong>layout_width & layout_height</strong></summary>

- <strong>layout_width</strong> dùng để quy định chiều cao của View
- <strong>layout_height</strong> dùng để quy định chiều rộng của View.
- Các View khi được khởi tạo bắt buộc phải có 2 yếu tố này
- <strong>match_parent</strong>: Các view sẽ có chiều bằng đúng chiều của phần tử cha của nó
- <strong>wrap_content</strong>: Chiều của view sẽ phụ thuộc vào content của view
- Cách truy cập:
```xml
<TextView
    android:layout_width = "wrap_content"
    android:layout_height = "match_parent">
```

</details>

<details>
<summary> <strong> id </strong></summary>

- Thuộc tính <strong> id </strong> xác định <strong> id </strong> của View, được khai báo ở file định nghĩa giao diện XML và sử dụng lại trong code Java để ánh xạ đối tượng, tìm kiếm các View trong code Java khi cần.
- Trong file XML:
```xml
<TextView
    android:id = "@+id/tv_message"
    android:layout_width = "wrap_content"
    android:layout_height = "match_parent">
```
- Ánh xạ đối tượng Textview trong XML thông qua id trong Java:
```java
TextView tvMessage = (TextView)findViewById(R.id.tv_message);
```
Phương thức `findViewById()` sẽ trả về về `View` cho nên cần ép kiểu về kiểu mong muốn, ở đây là `TextView`.
</details>

<details>
<summary> <strong> background </strong></summary>

Dùng để xác định màu nền của **View**
```xml
<TextView
    android:id = "@+id/tv_message"
    android:layout_width = "wrap_content"
    android:layout_height = "match_parent"
    android:background="#ffffff">
```
</details>

<details>
<summary> <strong> margin & padding </strong></summary>

- `margin`: là khoảng cách từ các cạnh của `View` hiện tại tới các `View` khác.
- `padding`: là khoảng cách từ các cạnh của `View` tới phần nội dung bên trong.
![](https://resources.iostream.co/content/article/5fa8912d5f20ad387a74c1f0/resources/res-1605193034-1605193034427.png)
- View chưa set padding & margin:
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/tv_message"
        android:text="Hello Android Studio"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="#D5BFE4"/>
</FrameLayout>
```

- View sau khi thêm `marginTop` và `marginLeft`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/tv_message"
        android:text="Hello Android Studio"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="50dp"
        android:layout_marginLeft="30dp"
        android:background="#D5BFE4"/>
</FrameLayout>
```

- View sau khi thêm `paddingTop` và `paddingBottom`
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/tv_message"
        android:text="Hello Android Studio"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="50dp"
        android:layout_marginLeft="30dp"
        android:paddingTop="20dp"
        android:paddingBottom="10dp"
        android:background="#D5BFE4"/>
</FrameLayout>
```
</details>

- 1 số view cơ bản trong Android:
