package contracts.api.locations.create

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return a created location."
    request {
        url("/api/locations/create/database1/bucket1")
        headers {
            header("x-auth-secret-key", "token1")
        }
        method POST()
    }
    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body([
                "databaseHash": "-7201775940730624163",
                "bucketHash"  : "7781363064255353970"
        ]
        )
    }
    input {
        triggeredBy('checkSuccess()')
    }
}