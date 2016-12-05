package models.dubbo.provider;


import com.alibaba.dubbo.config.*;
import com.dewmobile.common.service.EaseMobMessageService;
import com.dewmobile.common.service.EaseMobMessageServiceImpl;

/**
 * Created by acmac on 2016/06/16.
 */
public class DubboProviderHelper {

    public static void init() {

        System.out.println("DubboProviderHelper init start");

        // 服务实现
        EaseMobMessageService msgService = new EaseMobMessageServiceImpl();

        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("zapyaMsgSystem");

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol("zookeeper");
        registry.setAddress("zookeeper://172.16.10.47:2181");

        // 服务提供者协议配置
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("dubbo");
        protocol.setPort(19000);
        protocol.setThreads(200);

        // 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口

        // 服务提供者暴露服务配置
        ServiceConfig<EaseMobMessageService> service = new ServiceConfig<EaseMobMessageService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
        service.setApplication(application);
        service.setRegistry(registry); // 多个注册中心可以用setRegistries()
        service.setProtocol(protocol); // 多个协议可以用setProtocols()
        service.setInterface(EaseMobMessageService.class);
        service.setRef(msgService);
        service.setVersion("1.0.0");

        // 暴露及注册服务
        service.export();

        System.out.println("DubboProviderHelper init end");
    }

    public static void main(String[] args) {
        init();
    }

}
