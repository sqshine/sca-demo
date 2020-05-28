package com.sqshine.productservice.loadbalancer;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface ILoadBalancer {
    /**
     * 从注册中心服务列表中获取单个服务实例
     * @param serviceInstances 服务实例列表
     * @return 单个服务实例
     */
    ServiceInstance getSingleInstance(List<ServiceInstance> serviceInstances);
}
