package com.example.demo.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.pay.EcpayProps;
import com.example.demo.pay.EcpaySigner;
import com.example.demo.pay.PaymentParamsFactory;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentParamsFactory factory;
    private final EcpayProps props;
    private final ConcurrentHashMap<String, String> orderStatus = new ConcurrentHashMap<>();
    private final EcpaySigner signer;

    public PaymentController(PaymentParamsFactory factory, EcpayProps props) {
        this.factory = factory;
        this.props = props;
        this.signer = new EcpaySigner(props.getHashKey(), props.getHashIv());
    }

    /** 1) 建立訂單並回傳自動提交的 HTML 表單（導到綠界付款頁） */

    @PostMapping(value = "/checkout",produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String checkout(@RequestParam String orderId, @RequestParam int amount,
                           @RequestParam(defaultValue = "Demo 商品x1") String itemName, 
                           @RequestParam(defaultValue = "Demo 訂單")  String tradeDesc) {
        // 確保 MerchantTradeNo 唯一（可用你的 orderId 加上時間戳或隨機碼）
        String merchantTradeNo = ensureUniqueTradeNo(orderId);
        orderStatus.put(merchantTradeNo, "PENDING");

        Map<String, String> form = factory.buildCreditCheckoutParams(
                merchantTradeNo, amount, itemName, tradeDesc,
                props.getReturnUrl(), props.getResultUrl());

        // 回傳自動 submit 的 HTML
        return buildAutoPostHtml(factory.getCheckoutUrl(), form);
    }

    /** 2) Server 背景通知（ReturnURL） */
    @PostMapping("/return")
    @ResponseBody
    public String serverReturn(@RequestParam MultiValueMap<String, String> body) {
        Map<String, String> p = flat(body);

        // 驗簽
        String provided = p.getOrDefault("CheckMacValue", "");
        String computed = signer.genCheckMacValue(p);
        boolean ok = provided != null && provided.equalsIgnoreCase(computed);

        if (ok && "1".equals(p.get("RtnCode"))) {
            String tradeNo = p.get("MerchantTradeNo");
            if (tradeNo != null) orderStatus.put(tradeNo, "PAID");
            // 重要：回覆純文字 1|OK
            return "1|OK";
        }
        return "0|ERROR"; // 失敗就不要 1|OK
    }

    /** 3) Client 導回顯示結果（OrderResultURL） */
    @PostMapping("/result")
    @ResponseBody
    public String clientResult(@RequestParam MultiValueMap<String, String> body) {
        Map<String, String> p = flat(body);
        String tradeNo = p.getOrDefault("MerchantTradeNo", "UNKNOWN");
        String status = orderStatus.getOrDefault(tradeNo, "PENDING");
        return "<h2>付款結果</h2>"
                + "<p>MerchantTradeNo: " + tradeNo + "</p>"
                + "<p>Status: " + status + "</p>"
                + "<p>RtnMsg: " + p.getOrDefault("RtnMsg", "") + "</p>";
    }

    // ===== helpers =====
    private static Map<String, String> flat(MultiValueMap<String, String> mv) {
        Map<String, String> map = new HashMap<>();
        for (String k : mv.keySet()) map.put(k, mv.getFirst(k));
        return map;
    }


    private static String buildAutoPostHtml(String action, Map<String, String> fields) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body onload='document.forms[0].submit()'>")
          .append("<form method='post' action='").append(action).append("'>");
        for (Map.Entry<String, String> e : fields.entrySet()) {
            sb.append("<input type='hidden' name='").append(e.getKey())
              .append("' value='").append(escapeHtml(e.getValue())).append("'>");
        }
        sb.append("</form><p>Redirecting to ECPay...</p></body></html>");
        return sb.toString();
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
    private static String ensureUniqueTradeNo(String orderId) {
        return (orderId + System.currentTimeMillis()).substring(0, Math.min(20, (orderId + System.currentTimeMillis()).length()));
    }
}
