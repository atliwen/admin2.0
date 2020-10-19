//package com.example.admin2;
//
//import com.netflix.appinfo.InstanceInfo;
//import de.codecentric.boot.admin.server.cloud.discovery.DefaultServiceInstanceConverter;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
//import org.springframework.util.StringUtils;
//
//import java.net.URI;
//
///**
// * @author 李文
// * @create 2020-10-14 15:39
// **/
//public class ServiceInstanceConverter extends DefaultServiceInstanceConverter
//{
//    @Override
//    protected URI getHealthUrl(ServiceInstance instance) {
//        if (!(instance instanceof EurekaServiceInstance)) {
//            return super.getHealthUrl(instance);
//        }
//
//        InstanceInfo instanceInfo = ((EurekaServiceInstance) instance).getInstanceInfo();
//        String healthUrl = instanceInfo.getSecureHealthCheckUrl();
//        if (StringUtils.isEmpty(healthUrl)) {
//            healthUrl = instanceInfo.getHomePageUrl() + "health";
//        }
//        return URI.create(healthUrl);
//    }
//}
