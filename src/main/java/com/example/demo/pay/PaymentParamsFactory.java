package com.example.demo.pay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class PaymentParamsFactory {

    private final EcpayProps props;
    private final EcpaySigner signer;

    public PaymentParamsFactory(EcpayProps props) {
        this.props = props;
        this.signer = new EcpaySigner(props.getHashKey(), props.getHashIv());
    }

    public Map<String, String> buildCreditCheckoutParams(String tradeNo, int amount, String itemName,
                                                         String tradeDesc, String returnURL, String resultURL) {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("MerchantID", props.getMerchantId());
        m.put("MerchantTradeNo", tradeNo); // 必須唯一，≤20
        m.put("MerchantTradeDate", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        m.put("PaymentType", "aio");
        m.put("TotalAmount", String.valueOf(amount)); // 整數
        m.put("TradeDesc", tradeDesc);                // 送簽前即可（實務可先 URL encode，但這裡交由 signer 統一處理）
        m.put("ItemName", itemName);                  // 多品名用 # 分隔：蛋糕x1#咖啡x2
        m.put("ReturnURL", returnURL);                // Server 背景通知
        m.put("OrderResultURL", resultURL);           // Client 導回顯示頁
        m.put("ChoosePayment", "Credit");
        m.put("EncryptType", "1");                    // SHA256

        // 加上 CheckMacValue
        m.put("CheckMacValue", signer.genCheckMacValue(m));
        return m;
    }

    public String getCheckoutUrl() {
        return props.getCheckoutUrl();
    }
}
