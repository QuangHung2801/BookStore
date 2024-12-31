# 📚 **Cửa Hàng Bán Sách Online** 📚

Cửa Hàng Bán Sách Online là một ứng dụng e-commerce giúp người dùng tìm kiếm, chọn mua sách và thanh toán trực tuyến dễ dàng. Ứng dụng hỗ trợ nhiều phương thức thanh toán, bao gồm VNPay, và cho phép người dùng đăng nhập qua Google và Facebook. Thêm vào đó, người dùng có thể quản lý giỏ hàng, xem chi tiết sản phẩm, và theo dõi đơn hàng.

---

## 🎯 **Mô Tả Dự Án**

Cửa Hàng Bán Sách cung cấp các tính năng cơ bản sau:
- **Đăng nhập/Đăng ký:** Hỗ trợ đăng nhập qua Google và Facebook.
- **Mua sách:** Tìm kiếm sách, xem chi tiết sách, chọn số lượng và thêm vào giỏ hàng.
- **Giỏ hàng:** Quản lý sách trong giỏ, thay đổi số lượng hoặc xóa sách.
- **Thanh toán:** Hỗ trợ thanh toán trực tuyến qua VNPay.
- **Quản lý đơn hàng:** Theo dõi tình trạng đơn hàng, xem lịch sử mua hàng.
- **Quản lý sản phẩm:** Quản lý sách, thêm, sửa, xóa sách.
- **Quản lý người dùng:** Thêm, sửa, xóa người dùng và phân quyền cho người quản lý.

---

## 🚀 **Tính Năng Chính**

- **Đăng nhập/Đăng ký:**  
  Hỗ trợ đăng nhập qua các tài khoản Google và Facebook sử dụng Spring Security và OAuth2.
  
- **Mua sách và giỏ hàng:**  
  Người dùng có thể tìm kiếm, chọn sách, điều chỉnh số lượng và thêm vào giỏ hàng.

- **Thanh toán trực tuyến:**  
  Tích hợp VNPay API để thanh toán trực tuyến.

- **Quản lý đơn hàng:**  
  Người dùng có thể theo dõi đơn hàng của mình từ khi đặt cho đến khi giao hàng hoàn tất.

- **Quản lý sản phẩm:**  
  Quản trị viên có thể thêm, sửa và xóa sách trong hệ thống. Sản phẩm được quản lý chi tiết bao gồm tên, tác giả, mô tả, giá cả, hình ảnh.

- **Tìm kiếm sách:**  
  Tìm kiếm sách theo tên, tác giả hoặc thể loại.

- **Chia sẻ trên mạng xã hội:**  
  Người dùng có thể chia sẻ sách yêu thích qua các mạng xã hội.

---

## ⚙️ **Công Nghệ Sử Dụng**

### **Backend:**
- **Spring Boot:** Framework phát triển ứng dụng backend.
- **Spring Security:** Quản lý xác thực và phân quyền người dùng.
- **OAuth2:** Đăng nhập qua Google và Facebook.
- **VNPay API:** Xử lý thanh toán qua VNPay.
- **MySQL:** Cơ sở dữ liệu lưu trữ thông tin người dùng, sách, và đơn hàng.
- **Thymeleaf:** Templating engine cho frontend.

### **Frontend:**
- **HTML/CSS/JavaScript:** Tạo giao diện người dùng.
- **Thymeleaf:** Sử dụng để kết hợp dữ liệu động với HTML.

### **API và Tích Hợp:**
- **Google OAuth2:** Đăng nhập qua Google.
- **Facebook Login:** Đăng nhập qua Facebook.
- **VNPay API:** Xử lý thanh toán trực tuyến.

---

## 📋 **Chức Năng CRUD**

- **Sách:**
  - Tạo, đọc, sửa, xóa sách.
  - Hiển thị danh sách sách, thông tin chi tiết sách.
  
- **Giỏ hàng:**
  - Thêm sách vào giỏ, thay đổi số lượng, xóa sách khỏi giỏ.
  
- **Đơn hàng:**
  - Tạo đơn hàng khi thanh toán, theo dõi trạng thái đơn hàng.

- **Người dùng:**
  - Quản lý tài khoản người dùng, phân quyền quản trị cho người dùng có vai trò admin.

---

## 💳 **Đặt Lịch và Thanh Toán**

- **Đặt sách:**  
  Người dùng có thể chọn sách, thêm vào giỏ hàng và thanh toán.

- **Thanh toán VNPay:**  
  Tích hợp thanh toán qua VNPay API để xử lý các giao dịch.

- **Cập nhật trạng thái đơn hàng:**  
  Theo dõi tiến độ đơn hàng (Đang xử lý, Đã giao, Hoàn thành).

---

## 📊 **Thống Kê và Quản Lý**

- **Quản lý đơn hàng:**  
  Quản trị viên có thể theo dõi tất cả đơn hàng đã được tạo trong hệ thống.

- **Thống kê doanh thu:**  
  Hiển thị các biểu đồ và báo cáo doanh thu dựa trên các đơn hàng đã hoàn thành.

---

## 🛠️ **Cài Đặt và Chạy Dự Án**

### **Yêu Cầu Hệ Thống:**  
- **JDK 17+**  
- **Spring Boot 3.0+**  
- **MySQL 8.0+**  
- **Google Developer Console (để lấy thông tin OAuth2)**  
- **Facebook Developer Console (để lấy thông tin Facebook Login)**

### **Hướng Dẫn Cài Đặt:**

1. **Clone repository:**  
   ```bash
   git clone https://github.com/QuangHung2801/BookstoreApp.git

2. **Cài đặt Backend:**
Import dự án Spring Boot vào IDE (IntelliJ IDEA, Eclipse).
Cập nhật thông tin kết nối database trong application.properties:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookstore
spring.datasource.username=root
spring.datasource.password=yourpassword
server.servlet.session.timeout=30m
spring.session.store-type=jdbc
```
3. **Cài đặt Frontend:**

Cài đặt các thư viện phụ thuộc:
```
mvn clean install
```
Chạy ứng dụng Backend:
```
mvn spring-boot:run
```
🌟 Trải Nghiệm Dự Án
Ứng dụng Cửa Hàng Bán Sách Online cung cấp trải nghiệm người dùng tuyệt vời, dễ sử dụng và nhiều tính năng tiện ích cho việc mua sắm sách trực tuyến.
