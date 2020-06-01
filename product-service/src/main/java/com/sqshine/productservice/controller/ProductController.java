package com.sqshine.productservice.controller;

import com.sqshine.productservice.client.UserClient;
import com.sqshine.productservice.domain.User;
import com.sqshine.productservice.loadbalancer.ILoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

/**
 * @author sqshine
 */
@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {
    /**
     * 自动负载均衡，使用ribbon
     */
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ILoadBalancer loadBalancer;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private UserClient userClient;

    @GetMapping("/user/{id}")
    public User findById(@PathVariable Long id) {
        return restTemplate.getForObject("http://user-service/user/" + id, User.class);
    }

    @GetMapping("/user")
    public User findById2(@RequestParam("id") Long id) {
        return restTemplate.getForObject("http://user-service/user/" + id, User.class);
    }

    @GetMapping("/user/feign/{id}")
    public User findByIdFeign(@PathVariable Long id) {
        return userClient.findById(id);
    }

    @GetMapping("/user/feign")
    public User findByIdFeign2(@RequestParam("id") Long id) {
        return userClient.findById2(id);
    }

    @GetMapping("/user/fix")
    public String getUserService() {
        //1.根据服务名称从注册中心获取集群列表地址
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("user-service");
        //2.列表任意选择一个实现远程服务调用
        ServiceInstance instance = serviceInstances.get(0);
        URI uri = instance.getUri();
        RestTemplate restTemplateSimple = new RestTemplate();
        return restTemplateSimple.getForObject(uri + "/user/provider", String.class);
    }

    @GetMapping("/user/lb")
    public String getUserServiceByLb() {
        //1.根据服务名称从注册中心获取集群列表地址
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("user-service");
        //2.列表任意选择一个实现远程服务调用
        ServiceInstance instance = loadBalancer.getSingleInstance(serviceInstances);
        URI uri = instance.getUri();
        RestTemplate restTemplateSimple = new RestTemplate();
        return restTemplateSimple.getForObject(uri + "/user/provider", String.class);
    }

    @GetMapping("/user/lbc")
    public ServiceInstance getUserLoadBalancerClient() {
        //1.根据服务名称从注册中心获取集群列表地址
        return loadBalancerClient.choose("user-service");
    }
}