package com.sqshine.productservice.client;

import com.sqshine.productservice.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author sqshine
 */
@FeignClient("user-service")
@RequestMapping("/user")
public interface UserClient {
    /**
     * feign
     *
     * @return string
     */
    @GetMapping("/provider")
    String provider();

    /**
     * feign
     *
     * @param id userid
     * @return user
     */
    @GetMapping("/{id}")
    User findById(@PathVariable Long id);

    @GetMapping("/")
    User findById2(@RequestParam("id") Long id);
}
