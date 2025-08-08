package com.pm.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillingServiceGrpcCient {

    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    // localhost:9001/BillingService/CreatePatientAccount
    // aws.grpc:123123/BillingService/CreatePatientAccount
    public BillingServiceGrpcCient(@Value("${billing.service.address:localhost}") String serverAddress,
                                   @Value("${billing.service.grpc.port:9001}") int serverPort) {
        log.info("Connecting to Billing Service at {}:{}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();

        blockingStub = BillingServiceGrpc.newBlockingStub(channel);

    }
    public BillingResponse createBillingAccount(String patientId, String name, String email) {
        log.info("Creating billing account for patient: {}, {}", patientId, name);

        BillingRequest request = BillingRequest.newBuilder()
                .setParentId(patientId)
                .setName(name)
                .setEmail(email)
                .build();

        BillingResponse response = blockingStub.createBillingAccount(request);
        log.info("Billing account created with response: {}", response);
        log.info("Received response from billing service cia GRPC: {}", response);

        return response;

    }
}
