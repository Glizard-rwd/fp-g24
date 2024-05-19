Module: claim-maven
Basic-package:
- domain: gồm nhóm user(customer, provider, admin) class và các file khác (claim, policy, bank, document)
- logic: hanle tất cả function trong claim system application. Hiện tại mới tạm xong CRUD
+ userDB: create, update, delele, get user
+ claimDB: create, update, delete, get claim
+ policy: create, update, delete, get policy
+ etc
- UI: dùng để tạo GUI cho application (not done), dùng javafx
- [ ] Build SQL database + JDBC connection
  - host: localhost
  - port: 5432
  - database: fp24
  - username: postgres
  - password: 123456789
  - ![img.png](img.png)
  - Cần phải install postgresql vào pc + tạo datasource postgresql trong intellij. Nếu dùng intellij community cần phải cài Database Navigator plugin để access database

- class diagram: https://drive.google.com/file/d/1_GIfvod-virmi4m8KkfodNMmRoyGPZ9J/view?usp=sharing. Trong file có code SQL để tạo table
- [ ] install external library: postgresql-42.2.23.jar + maven jar + openjdk-22
- Test DB class: (currently in Main.java): Hiện tại mới test dữ liệu trên main class
  - [ ] add: new policyHolder, new policyOwner, new claim - OK
  - [x] update: OK
  - [x] delete: OK
  - [x] getOne: OK
  - [x] getAll: OK
    Sẽ vừa làm GUI vừa check + sửa lại

To do:
- [ ] Class admin: Tạo class Admin hoàn chỉnh
- [ ] CRUD dependent: Chưa kết nối dependent với policy holder
- [x] update time when user change data
- [x] track user action in log table (only admin access)
- [ ] statistic (only admin access)
- [ ] GUI for each user group: Dự kiến tạo CustomerGUI, ProviderGUI và AdminGUI
- [ ] function cho mỗi nhóm:
  + Mỗi user có thể login, log out + get info
  + Chỉ có thể tạo tài khoản cho nhóm Provider và Customer. Nhóm admin sẽ dùng tài khoản mặc định. Khi tạo tài khoản cần tạo user_info trước rồi mới tạo thông tin khác (customer cần tạo thêm bank)
  + PolicyHolder có thể create, update, get claim (trừ delete) (claim_status = new)
  + PolicyOwner tạo policy + quản lý tài khoản (CRUD) cho nhóm PolicyHolder + Dependent phụ thuộc vào nó + quản lý claim (CRUD) + gửi claim cho Surveyor
    Nếu policy owner mà xóa tài khoản của holder hay dependent nào thì tài khoản đó bị xóa khỏi hệ thống
  + Surveryor tiếp nhận claim + chuyển cho Manager (claim status = processing). Retrieve info của claim + customer của surveryor đó.
  + Imanager cũng retreive info của customer + claim + approve or deny (khi đó claim_status = done)
  + Admin quản lý tất cả entity nhưng chỉ có thể retrieve thông tin của claim
    - [ ]...
