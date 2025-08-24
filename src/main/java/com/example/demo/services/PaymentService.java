package com.example.demo.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.bean.OrderBean;
import com.example.demo.dto.PaymentCheckoutRequest;
import com.example.demo.enums.OrderStatus;
import com.example.demo.pay.EcpayProps;
import com.example.demo.pay.EcpaySigner;
import com.example.demo.pay.PaymentParamsFactory;
import com.example.demo.repository.OrderRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Service
public class PaymentService {

    // private ConcurrentHashMap<String, String> orderStatus = new ConcurrentHashMap<>();

    private final OrderRepository orderRepository;
    private final PaymentParamsFactory factory;
    private final EcpayProps props;
    private final EcpaySigner signer;

    public PaymentService(PaymentParamsFactory factory, EcpayProps props, OrderRepository orderRepository) {
        this.factory = factory;
        this.props = props;
        this.signer = new EcpaySigner(props.getHashKey(), props.getHashIv());
        this.orderRepository = orderRepository;
    }

    public String checkout(Long userId, PaymentCheckoutRequest request) {

        // orderStatus.put(merchantTradeNo, "PENDING");
        Map<String, String> form = factory.buildCreditCheckoutParams(
                request.getMerchantTradeNo(), request.getAmountCents(), request.getItemName(), request.getTradeDesc(),
                props.getReturnUrl(), props.getResultUrl());

        return buildAutoPostHtml(factory.getCheckoutUrl(), form);
    }

    @Transactional
    public String serverReturn(@RequestParam MultiValueMap<String, String> body) {
        Map<String, String> p = flat(body);
        // 驗簽
        String provided = p.getOrDefault("CheckMacValue", "");
        String computed = signer.genCheckMacValue(p);
        boolean ok = provided != null && provided.equalsIgnoreCase(computed);

        if (ok && "1".equals(p.get("RtnCode"))) {
            String tradeNo = p.get("MerchantTradeNo");
            OrderBean order = orderRepository.findByMerchantTradeNo(tradeNo);
            order.setStatus(OrderStatus.PAID);
            // if (tradeNo != null) orderStatus.put(tradeNo, "PAID");
            // 重要：回覆純文字 1|OK
            return "1|OK";
        }
        return "0|ERROR"; // 失敗就不要 1|OK
    }

    public void clientResult(@RequestParam MultiValueMap<String, String> body) throws IOException {
        Map<String, String> p = flat(body);
        String tradeNo = p.getOrDefault("MerchantTradeNo", "UNKNOWN");
        String status = "PENDING"; // orderStatus.getOrDefault(tradeNo, "PENDING");

        // return "<h2>付款結果</h2>"
        //         + "<p>MerchantTradeNo: " + tradeNo + "</p>"
        //         + "<p>Status: " + status + "</p>"
        //         + "<p>RtnMsg: " + p.getOrDefault("RtnMsg", "") + "</p>";
    }

    // ===== helpers =====
    public Map<String, String> flat(MultiValueMap<String, String> mv) {
        Map<String, String> map = new HashMap<>();
        for (String k : mv.keySet()) map.put(k, mv.getFirst(k));
        return map;
    }


    public String buildAutoPostHtml(String action, Map<String, String> fields) {
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

    public String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    public String ensureUniqueTradeNo(String orderId) {
        return (orderId + System.currentTimeMillis()).substring(0, Math.min(20, (orderId + System.currentTimeMillis()).length()));
    }
}
