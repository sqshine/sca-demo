package com.sqshine.userservice.controller;

import com.sqshine.userservice.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author sqshine
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    /**
     * 服务注册的实例
     */
    @Autowired
    private Registration registration;

    /**
     * 服务发现客户端
     */
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/provider")
    public String provider() {
        ServiceInstance instance = serviceInstance();
        log.info("provider service, host = {}，port = {}, service_id ={}", instance.getHost(), instance.getPort(), instance.getServiceId());
        return "Hello,Provider!" + registration.getHost() + ":" + registration.getPort();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        log.info("{} 被调用了", registration.getPort());
        for (User user : getUsers()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @GetMapping("/")
    public User findById2(@RequestParam("id") Long id) {
        for (User user : getUsers()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return new User();
    }

    @PostMapping("/")
    public User postUser(@RequestBody User user) {
        return user;
    }

    @GetMapping("/list")
    public List<User> listAll() {
        return getUsers();

    }

    private List<User> getUsers() {
        List<User> list = new ArrayList<>();
        User user = new User(1L, "zs", "zhangsan", 20, new BigDecimal(10000));
        User user2 = new User(2L, "ls", "lisi", 201, new BigDecimal(20000));
        User user3 = new User(3L, "ww", "wangwu", 210, new BigDecimal(50000));
        list.add(user);
        list.add(user2);
        list.add(user3);
        return list;
    }

    /**
     * 获取当前服务的服务实例
     *
     * @return ServiceInstance
     */
    public ServiceInstance serviceInstance() {
        List<ServiceInstance> list = discoveryClient.getInstances(registration.getServiceId());
        if (list != null && list.size() > 0) {
            //return list.get(0);

            //随机获取一个实例
            int i = new Random().nextInt(list.size());
            return list.get(i);
        }
        return null;
    }

}
