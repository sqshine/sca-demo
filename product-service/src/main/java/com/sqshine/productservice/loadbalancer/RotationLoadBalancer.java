package com.sqshine.productservice.loadbalancer;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询算法
 *
 * @author sqshine
 */
@Component
public class RotationLoadBalancer implements ILoadBalancer {

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public ServiceInstance getSingleInstance(List<ServiceInstance> serviceInstances) {
        int index = atomicInteger.incrementAndGet() % serviceInstances.size();
        return serviceInstances.get(index);
    }
}
