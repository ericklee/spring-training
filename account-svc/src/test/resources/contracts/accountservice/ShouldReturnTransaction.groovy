package contracts.accountservice

import org.springframework.cloud.contract.spec.Contract

Contract.make {

    request {
        url("/api/transactions/1")
        method("GET")
    }

    response {
        status 200
        body(
                transactionId: "1",
                accountId: "ACC1234567",
                transactionType: "DEPOSIT",
                amount: 10,
                description: "Token Amount"
        )
        headers {
            contentType(applicationJson())
        }
    }

}