package com.db.product;

import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.exception.NacosException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@SpringBootTest
public class DiscoveryTest {
  @Autowired
  DiscoveryClient discoveryClient;
  @Autowired
  NacosServiceDiscovery nacosServiceDiscovery;

  @Test
  void discoveryClient() {
    System.out.println("服务列表：" + discoveryClient.getServices());
    discoveryClient.getServices().forEach(serviceId -> {
      System.out.println("服务ID：" + serviceId);
      discoveryClient.getInstances(serviceId).forEach(instance -> {
        System.out.println("实例信息：");
        System.out.println("  服务ID：" + instance.getServiceId());
        System.out.println("  主机名：" + instance.getHost());
        System.out.println("  端口号：" + instance.getPort());
        System.out.println("  URI：" + instance.getUri());
      });

    });

  }

  @Test
  void nacosServiceDiscovery() throws NacosException {
    System.out.println("Nacos服务发现：" + nacosServiceDiscovery.getServices());
    nacosServiceDiscovery.getServices().forEach(serviceId -> {
      System.out.println("Nacos服务ID：" + serviceId);
      try {
        nacosServiceDiscovery.getInstances(serviceId).forEach(instance -> {
          System.out.println("Nacos实例信息：");
          System.out.println("  服务ID：" + instance.getServiceId());
          System.out.println("  主机名：" + instance.getHost());
          System.out.println("  端口号：" + instance.getPort());
          System.out.println("  URI：" + instance.getUri());
        });
      } catch (NacosException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
