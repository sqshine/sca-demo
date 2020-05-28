package com.sqshine.productservice.controller;

import com.sqshine.productservice.domain.User;
import com.sqshine.productservice.loadbalancer.ILoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class ProductController {
    /**
     * 自动负载均衡，使用ribbon
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 无负载均衡
     */
    @Autowired
    private RestTemplate restTemplate1;

    @Autowired
    private ILoadBalancer loadBalancer;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/user/{id}")
    public User findById(@PathVariable Long id) {
        return restTemplate.getForObject("http://user-service/user/" + id, User.class);
    }

    @GetMapping("/user/fix")
    public String getUserService() {

        //1.根据服务名称从注册中心获取集群列表地址
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("user-service");
        //2.列表任意选择一个实现远程服务调用
        ServiceInstance instance = serviceInstances.get(0);
        URI uri = instance.getUri();
        return restTemplate1.getForObject(uri + "/user/provider", String.class);
    }

    @GetMapping("/user/lb")
    public String getUserServiceByLB() {

        //1.根据服务名称从注册中心获取集群列表地址
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("user-service");
        //2.列表任意选择一个实现远程服务调用
        ServiceInstance instance = loadBalancer.getSingleInstance(serviceInstances);
        URI uri = instance.getUri();
        return restTemplate1.getForObject(uri + "/user/provider", String.class);
    }
}