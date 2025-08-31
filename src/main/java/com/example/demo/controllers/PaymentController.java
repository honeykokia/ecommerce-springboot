package com.example.demo.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PaymentCheckoutRequest;
import com.example.demo.services.PaymentService;

import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /** 1) 建立訂單並回傳自動提交的 HTML 表單（導到綠界付款頁） */

    @PostMapping(value = "/checkout",produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String checkout(@AuthenticationPrincipal Long userId, @RequestBody PaymentCheckoutRequest paymentCheckoutRequest) {

        return paymentService.checkout(userId, paymentCheckoutRequest);
    }

    /** 2) Server 背景通知（ReturnURL） */
    @PostMapping("/return")
    @ResponseBody
    public String serverReturn(@RequestParam MultiValueMap<String, String> body) {
        return paymentService.serverReturn(body);
    }

    /** 3) Client 導回顯示結果（OrderResultURL） */
    @PostMapping("/result")
    @ResponseBody
    public ResponseEntity<?> clientResult(@RequestParam MultiValueMap<String, String> body,HttpServletResponse response) {
        String html =
            "<!doctype html><meta charset='utf-8'>" +
            "<script>(function(){ " +
            " try{ window.close(); }catch(e){}" +
            // 若瀏覽器禁止 close（例如不是由 script 打開），給個備援導回你的前端
            " setTimeout(function(){ location.href='http://localhost:5173/#/payment-result'; }, 500);" +
            "})();</script>" +
            "<body>正在返回商店…</body>";
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
    }

}
