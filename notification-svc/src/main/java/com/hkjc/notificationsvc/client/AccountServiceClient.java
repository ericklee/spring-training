package com.hkjc.notificationsvc.client;

import com.hkjc.accountsvc.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account-service", url = "${account-service-url}", fallback = AccountServiceClient.AccountServiceClientFallback.class)
public interface AccountServiceClient {

    @GetMapping("/api/transactions/{transactionId}")
    TransactionDto getTransaction(@PathVariable Long transactionId);

    @Component
    class AccountServiceClientFallback implements AccountServiceClient {

        @Override
        public TransactionDto getTransaction(Long transactionId) {
            System.out.println("fallback!!!!!!!!!!");
            return new TransactionDto();
        }
    }
}
