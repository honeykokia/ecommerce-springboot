# Spring Boot 全域錯誤攔截處理器 (Global Exception Handler)

這個全域錯誤攔截處理器為 Spring Boot 電商應用提供統一的錯誤處理機制。

## 功能特色

### 1. 全面的錯誤處理
- ✅ 表單驗證錯誤處理 (@Valid)
- ✅ 參數類型錯誤處理
- ✅ 缺少必要參數處理
- ✅ HTTP 方法不支援處理
- ✅ JSON 格式錯誤處理
- ✅ 404 資源不存在處理
- ✅ 業務邏輯異常處理
- ✅ 運行時異常處理
- ✅ 其他未預期異常處理

### 2. 統一的響應格式
所有錯誤響應都遵循統一的 ApiResponse 格式：
```json
{
  "data": {
    "error": "錯誤代碼",
    "message": "錯誤訊息",
    "path": "請求路徑",
    "status": 400,
    "其他相關欄位": "額外資訊"
  },
  "timestamp": "2025-07-11T13:48:47.659758426"
}
```

### 3. 適當的 HTTP 狀態碼
- 400 Bad Request: 參數驗證失敗、參數錯誤
- 401 Unauthorized: 未授權訪問
- 404 Not Found: 資源不存在
- 405 Method Not Allowed: HTTP 方法不支援
- 500 Internal Server Error: 系統錯誤

## 使用示例

### 1. 驗證錯誤
```bash
curl -X POST http://localhost:8080/test/validation \
  -H "Content-Type: application/json" \
  -d '{"name":"","email":"invalid-email","age":null}'
```

響應：
```json
{
  "data": {
    "error": "VALIDATION_ERROR",
    "message": "請求參數驗證失敗",
    "fields": {
      "name": "姓名不能為空",
      "age": "年齡不能為空",
      "email": "電子郵件格式不正確"
    },
    "path": "/test/validation",
    "status": 400
  },
  "timestamp": "2025-07-11T13:47:50.403771077"
}
```

### 2. 參數類型錯誤
```bash
curl http://localhost:8080/test/type-mismatch?number=notanumber
```

響應：
```json
{
  "data": {
    "error": "TYPE_MISMATCH",
    "message": "參數 'number' 的類型不正確",
    "parameter": "number",
    "expectedType": "Integer",
    "path": "/test/type-mismatch",
    "status": 400
  },
  "timestamp": "2025-07-11T13:47:35.509128221"
}
```

### 3. 業務邏輯異常
```bash
curl http://localhost:8080/test/business-exception?type=user-not-found
```

響應：
```json
{
  "data": {
    "error": "USER_NOT_FOUND",
    "message": "用戶不存在: 12345",
    "path": "/test/business-exception",
    "status": 404
  },
  "timestamp": "2025-07-11T13:47:57.70449653"
}
```

### 4. 404 錯誤
```bash
curl http://localhost:8080/nonexistent
```

響應：
```json
{
  "data": {
    "error": "NOT_FOUND",
    "message": "請求的資源不存在",
    "path": "/nonexistent",
    "method": "GET",
    "status": 404
  },
  "timestamp": "2025-07-11T13:48:05.444352638"
}
```

## 業務異常使用

### BusinessException 類別提供便利的工廠方法：

```java
// 用戶不存在
throw BusinessException.userNotFound("12345");

// 商品不存在
throw BusinessException.productNotFound("PROD001");

// 庫存不足
throw BusinessException.insufficientStock("PROD002");

// 未授權訪問
throw BusinessException.unauthorizedAccess();

// 無效憑證
throw BusinessException.invalidCredentials();

// 訂單不存在
throw BusinessException.orderNotFound("ORD001");

// 付款失敗
throw BusinessException.paymentFailed("信用卡被拒絕");
```

### 自定義業務異常：
```java
throw new BusinessException("CUSTOM_ERROR", "自定義錯誤訊息", HttpStatus.BAD_REQUEST);
```

## 配置要求

### 1. 依賴項
確保 pom.xml 包含以下依賴：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### 2. 應用配置
在 application.properties 中啟用 404 異常處理：
```properties
spring.mvc.throw-exception-if-no-handler-found=true
spring.web.resources.add-mappings=false
```

## 日誌記錄

- **WARN 級別**: 客戶端錯誤 (400-499)
- **ERROR 級別**: 伺服器錯誤 (500+)

錯誤處理器會自動記錄所有異常，並包含請求路徑和錯誤詳情。

## 擴展指南

### 添加新的異常處理：
```java
@ExceptionHandler(CustomException.class)
public ResponseEntity<ApiResponse> handleCustomException(
        CustomException ex, HttpServletRequest request) {
    
    log.warn("Custom exception: {}", ex.getMessage());
    
    Map<String, Object> errorDetails = new HashMap<>();
    errorDetails.put("error", "CUSTOM_ERROR");
    errorDetails.put("message", ex.getMessage());
    errorDetails.put("path", request.getRequestURI());
    errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
    
    ApiResponse response = new ApiResponse(errorDetails);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}
```

## 測試覆蓋

✅ 所有異常類型都經過充分測試
✅ 響應格式一致性已驗證
✅ HTTP 狀態碼正確性已確認
✅ 現有功能無影響

這個全域錯誤處理器提供了強大且一致的錯誤處理機制，提升了 API 的可靠性和用戶體驗。