package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PaymentCheckoutRequest;
import com.example.demo.services.PaymentService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /** 1) 建立訂單並回傳自動提交的 HTML 表單（導到綠界付款頁） */

    @PostMapping(value = "/checkout",produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String checkout(@AuthenticationPrincipal Long userId, @RequestBody PaymentCheckoutRequest request) {
        return paymentService.checkout(userId, request);
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
    public String clientResult(@RequestParam MultiValueMap<String, String> body) {
        return paymentService.clientResult(body);
    }

}
