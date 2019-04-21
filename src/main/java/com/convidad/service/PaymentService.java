package com.convidad.service;

import com.convidad.bankA.client.BankAClient;
import com.convidad.bankA.common.dto.Payment;
import com.convidad.client.TrackerClient;
import com.convidad.dto.PaymentDTO;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
    private BankAClient bankAClient;

    @Autowired
    private TrackerClient trackerClient;

    private Map<String, List<PaymentDTO>> transactionsPerAccount = new HashMap<>();

    private List<PaymentDTO> failedTransactions = new ArrayList<>();

    public void payment(PaymentDTO paymentDTO) {

        Payment payment = createPayment(paymentDTO);
        bankAClient.transferMoney(payment);

        storeTransaction(paymentDTO.getAccountFrom(), paymentDTO);
        storeTransaction(paymentDTO.getAccountTo(), paymentDTO);
        trackerClient.registerTransaction(paymentDTO);
    }

    public List<PaymentDTO> getTransactions(String account) {

        trackerClient.registerTransactionRequestPerAccount(account);
        return transactionsPerAccount.get(account);
    }

    private void storeTransaction(String account, PaymentDTO paymentDTO) {

        transactionsPerAccount.compute(account, (accountFrom, paymentDTOS) -> {
            List<PaymentDTO> result = paymentDTOS;
            if (result == null) {
                result = new ArrayList<>();
            }
            result.add(paymentDTO);

            return result;
        });
    }

    private Payment createPayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setAccountNumberFrom(paymentDTO.getAccountFrom());
        payment.setAccountNumberTo(paymentDTO.getAccountTo());
        payment.setQuantity(paymentDTO.getQuantity());
        return payment;
    }


}
