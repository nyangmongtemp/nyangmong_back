package com.playdata.mainservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service")
public interface UserServiceClient {
}
