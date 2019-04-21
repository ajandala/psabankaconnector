package com.convidad.client;

import com.convidad.dto.PaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "tracker", url = "localhost:8002")
public interface TrackerClient {

    @PostMapping(value = "/registerTransaction")
    void registerTransaction(@RequestBody PaymentDTO transaction);

    @PostMapping(value = "/registerTransactionRequestPerAccount")
    void registerTransactionRequestPerAccount(@RequestBody String account);

    @PostMapping(value = "/registerFailedTransactionsRequest")
    void registerFailedTransactionsRequest();
}


