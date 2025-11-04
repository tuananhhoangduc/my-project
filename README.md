## ProjectJava — Ứng dụng quản lý bệnh viện (Java Swing + MySQL)

### Giới thiệu
Ứng dụng desktop quản lý bệnh viện xây dựng bằng Java Swing, tổ chức theo mô hình DAO/Service/View. Dự án hỗ trợ các nghiệp vụ cơ bản:
- Đăng nhập (`view/LoginForm` → `view/Loading` → `view/home`)
- Quản lý tài khoản người dùng (`view/account`)
- Quản lý bác sĩ (`view/doctor`)
- Quản lý bệnh nhân (`view/patient`)
- Quản lý phòng/giường (`view/room`, `view/roompanel`)
- Quản lý hồ sơ bệnh án (`view/medicalRecord`)
- Thanh toán (`view/payment`)

Dữ liệu kết nối tới MySQL thông qua `connection.JDBCConnection` và các lớp DAO trong thư mục `src/connection`.

### Kiến trúc & Công nghệ
- **Ngôn ngữ**: Java (thiết lập hiện tại `javac.source=24`, `javac.target=24`)
- **UI**: Java Swing (NetBeans GUI Builder, AbsoluteLayout)
- **CSDL**: MySQL (trình điều khiển MySQL Connector/J 9.4.0)
- **Build**: Apache Ant (tệp `build.xml`, cấu hình NetBeans trong `nbproject/`)

### Yêu cầu hệ thống
- JDK 24 (khuyến nghị). Nếu bạn dùng JDK thấp hơn (ví dụ 17/21), hãy chỉnh `javac.source` và `javac.target` trong `nbproject/project.properties` cho phù hợp.
- MySQL Server 8.x
- MySQL Connector/J (đi kèm bản phát hành trong `dist/lib/mysql-connector-j-9.4.0.jar`, hoặc tự thêm vào Libraries khi mở bằng NetBeans)

### Cài đặt cơ sở dữ liệu
1. Tạo CSDL MySQL (ví dụ tên `hospitaldb_basic`).
2. Import schema hoặc dữ liệu mẫu từ file sql `Data.sql` . Bạn có thể tự tạo các bảng phù hợp với các thực thể: `Account`, `User`, `Doctor`, `Patient`, `Room`, `MedicalRecord`.
4. Cập nhật cấu hình kết nối trong tệp:
   - `src/connection/JDBCConnection.java`
   - Các thông số mặc định hiện tại:
     - URL: `jdbc:mysql://127.0.0.1:3306/hospitaldb_basic`
     - user: `root`
     - password: (hiện đang đặt cứng trong mã nguồn — bạn nên thay đổi cho môi trường của mình)

Ví dụ thay đổi nhanh trong `JDBCConnection.java`:
```java
final String url = "jdbc:mysql://127.0.0.1:3306/hospitaldb_basic";
final String user = "YOUR_USER";
final String password = "YOUR_PASSWORD";
```

Lưu ý bảo mật: không commit mật khẩu thật lên GitHub. Bạn có thể chuyển sang đọc biến môi trường hoặc file cấu hình riêng (không theo dõi bởi Git).

### Thiết lập thư viện MySQL Connector/J
- Khi mở dự án bằng NetBeans: Add Library/JAR → trỏ tới `dist/lib/mysql-connector-j-9.4.0.jar` (hoặc tải bản mới nhất và thêm vào).
- Trong `nbproject/project.properties` có cấu hình đường dẫn JAR theo ổ đĩa cục bộ. Bạn nên chỉnh lại sang đường dẫn tương đối hoặc thêm lại thư viện qua IDE để tránh lỗi.

### Cách build và chạy

#### Cách 1: Chạy trong NetBeans (khuyến nghị cho phát triển)
1. Mở dự án `projectCSDL`.
2. Đảm bảo đã add MySQL Connector/J vào Libraries.
3. Cập nhật kết nối DB như phần trên.
4. Run Project (F6). Main class được cấu hình là `view.Loading`.

#### Cách 2: Dùng Ant trong dòng lệnh
Yêu cầu đã cài Ant (thường NetBeans đính kèm). Trong thư mục gốc dự án:
```bash
ant clean jar
```
Sau khi build thành công, JAR nằm tại `dist/projectCSDL.jar`. Để chạy:
```bash
# Windows (PowerShell/CMD)
java -cp "dist/projectCSDL.jar;dist/lib/*" view.Loading

# macOS/Linux (bash/zsh)
java -cp "dist/projectCSDL.jar:dist/lib/*" view.Loading
```

#### Cách 3: Chạy JAR đã phát hành
1. Đảm bảo có thư mục `dist/lib/` chứa `mysql-connector-j-9.4.0.jar`.
2. Sử dụng lệnh chạy như ở trên với classpath bao gồm `dist/lib/*`.

### Cấu trúc thư mục chính
- `src/connection`: Lớp kết nối và các DAO (AccountDao, DoctorDao, ...)
- `src/service`: Lớp nghiệp vụ (AccountService, DoctorService, ...)
- `src/model`: Thực thể dữ liệu (Account, Doctor, Patient, Room, MedicalRecord, User)
- `src/view`: Giao diện Swing (form + logic)
- `build.xml`: Script Ant
- `nbproject/`: Cấu hình NetBeans
- `dist/`: Sản phẩm build (JAR) và thư viện đi kèm

### Ghi chú quan trọng
- Nếu gặp lỗi biên dịch do phiên bản JDK, chỉnh `javac.source` và `javac.target` trong `nbproject/project.properties` cho khớp phiên bản JDK bạn dùng.
- Nếu gặp lỗi thiếu driver MySQL, kiểm tra classpath có chứa `mysql-connector-j-*.jar` khi chạy.
- Cập nhật thông tin đăng nhập DB trong `JDBCConnection.java` trước khi chạy.


