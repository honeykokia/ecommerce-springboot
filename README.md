# E-Commerce Spring Boot

## 📖 專案簡介
本專案為基於 **Spring Boot 3** 的電商後端系統，提供完整的 RESTful API，涵蓋 **使用者管理、商品管理、訂單處理、支付整合** 等功能。  
系統採用 **前後端分離架構**，並整合 **PostgreSQL** 作為主要資料庫，支援 JWT 驗證、交易狀態追蹤與金流回調處理。

---

## 🛠 技術棧
- **Backend Framework**: Spring Boot 3, Spring MVC  
- **Security & Auth**: Spring Security, JWT, Password Hashing (BCrypt)  
- **Database**: PostgreSQL, Spring Data JPA (Hibernate)  
- **Payment Gateway**: 綠界 ECPay API（CheckMacValue 驗簽、callback/result 流程）  
- **Other Tools**: Lombok, MapStruct, Maven  
- **Deployment**: AWS EC2 + RDS, Nginx (反向代理)

---

## 📂 專案架構
- **前端**：[ecommerce-vue](https://github.com/honeykokia/ecommerce-vue) (Vue 3 + TailwindCSS)  
- **後端**：[ecommerce-springboot](https://github.com/honeykokia/ecommerce-springboot) (Spring Boot + PostgreSQL + JWT)  
- **文件**：[ecommerce-docs](https://github.com/honeykokia/ecommerce-docs) (API 規格、資料庫結構、功能模組設計)  

---

## 📦 功能模組
- **使用者模組**：註冊 / 登入 / 忘記密碼 / 個人資訊管理 / 購買紀錄  
  - 技術重點：JWT 驗證、信箱驗證、BCrypt 密碼加密  
- **商品模組**：商品列表 / 詳情 / 分類 / 搜尋  
  - 技術重點：JPA 分頁查詢、動態搜尋條件  
- **活動模組**：活動列表 / 詳情 / 分類  
- **購物車模組**：加入 / 移除 / 修改商品、購物車清單  
- **訂單模組**：建立訂單 / 訂單詳情 / 狀態追蹤  
  - 技術重點：交易狀態機設計、避免併發衝突  
- **支付模組**：整合綠界 ECPay  
  - 技術重點：`CheckMacValue` 驗簽、callback / result 回調處理  
- **管理後台模組**：商品 / 訂單 / 使用者 / 活動 / 類別管理  

---

## 🔧 環境變數設定
請在專案根目錄建立 `.env` 檔案，並設定以下變數：

```bash
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://<DB_HOST>/<DB_NAME>
SPRING_DATASOURCE_USERNAME=<DB_USERNAME>
SPRING_DATASOURCE_PASSWORD=<DB_PASSWORD>

# Email
SPRING_EMAIL_HOST=smtp.gmail.com
SPRING_EMAIL_PORT=587
SPRING_EMAIL_USERNAME=<EMAIL_USERNAME>
SPRING_EMAIL_PASSWORD=<EMAIL_PASSWORD>

# ECPay
SPRING_ECPAY_MERCHANT_ID=<MERCHANT_ID>
SPRING_ECPAY_HASH_KEY=<HASH_KEY>
SPRING_ECPAY_HASH_IV=<HASH_IV>
SPRING_ECPAY_CHECKOUT_URL=<CHECKOUT_URL>
SPRING_ECPAY_RETURN_URL=<RETURN_URL>
SPRING_ECPAY_RESULT_URL=<RESULT_URL>
```
---

## 🚀 開發與部署
### 開發環境
1. 安裝必要工具：JDK 17、Maven、PostgreSQL
2. 啟動專案：
   ```bash
   ./mvnw spring-boot:run
   ```

### 部署環境
1. 編譯專案：
   ```bash
   ./mvnw clean package
   ```
2. 部署 JAR 檔案至伺服器：
   ```bash
   java -jar target/demo.jar
   ```

---

## 📑 相關文件
| 文件類型       | 位置                          |
|----------------|-------------------------------|
| API 規格       | [docs/api-spec.yaml](ecommerce-docs/docs/api-spec.yaml) |
| 資料庫 Schema  | [docs/database-schema.sql](ecommerce-docs/docs/database-schema.sql) |
| ER 圖          | [docs/er-diagram.puml](ecommerce-docs/docs/er-diagram.puml) |
| 功能模組       | [docs/feature-modules.yaml](ecommerce-docs/docs/feature-modules.yaml) |
| 用例圖         | [docs/feature-usecase.puml](ecommerce-docs/docs/feature-usecase.puml) |

---
