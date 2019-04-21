package com.convidad.controller;

import com.convidad.bankA.client.BankAClient;
import com.convidad.dto.PaymentDTO;
import com.convidad.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/payment")
    public void payment(@RequestBody PaymentDTO paymentDTO) {
        paymentService.payment(paymentDTO);
    }

    @GetMapping(value = "/transactions/{account}")
    public List<PaymentDTO> getTransactions(@PathVariable String account) {
        return paymentService.getTransactions(account);
    }

}
