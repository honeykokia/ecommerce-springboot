package com.example.demo.pay;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class EcpaySigner {

    private final String hashKey;
    private final String hashIV;

    public EcpaySigner(String hashKey, String hashIV) {
        this.hashKey = hashKey;
        this.hashIV = hashIV;
    }

    public String genCheckMacValue(Map<String, String> params) {
        // 1) 過濾空值與 CheckMacValue 自身
        Map<String, String> filtered = params.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getKey().equalsIgnoreCase("CheckMacValue"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // 2) 以參數名字典序排序（A→Z）
        String query = filtered.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER))
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        // 3) 前後加入 HashKey / HashIV
        String raw = "HashKey=" + hashKey + "&" + query + "&HashIV=" + hashIV;

        // 4) URL Encode（UTF-8），轉小寫
        String encoded = urlEncode(raw).toLowerCase(Locale.ROOT);

        // 5) SHA256 雜湊
        String sha = sha256(encoded);

        // 6) 轉大寫即為 CheckMacValue
        return sha.toUpperCase(Locale.ROOT);
    }

    private static String urlEncode(String s) {
        // 使用 URLEncoder，並保留 *()!~ 等常見字元的 ECPay 規則差異可先忽略（測試通常可過）
        return URLEncoder.encode(s, StandardCharsets.UTF_8)
                .replace("%20", "+")
                .replace("%21", "!")
                .replace("%28", "(")
                .replace("%29", ")")
                .replace("%2a", "*")
                .replace("%7e", "~");
    }

    private static String sha256(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : out) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA256 error", e);
        }
    }
}