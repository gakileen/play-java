package models.dubbo.consumer;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.dewmobile.common.service.EaseMobMessageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acmac on 2016/06/16.
 */
public class DubboConsumerHelper {

    private static ReferenceConfig<EaseMobMessageService> msgReference;

    public static void init() {

        System.out.println("DubboConsumerHelper init start");

        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("zapyaMsgSystem");

        // 连接注册中心配置
        List<RegistryConfig> registries = new ArrayList<RegistryConfig>();
        // test
        registries.add(new RegistryConfig("zookeeper://172.16.10.47:2181"));
        // prod
//        registries.add(new RegistryConfig("zookeeper://10.10.1.6:2181"));
//        registries.add(new RegistryConfig("zookeeper://10.10.1.7:2181"));
//        registries.add(new RegistryConfig("zookeeper://10.10.1.8:2181"));


        // 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接

        // 引用远程服务
        msgReference = new ReferenceConfig<EaseMobMessageService>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        msgReference.setApplication(application);
        msgReference.setRegistries(registries); // 多个注册中心可以用setRegistries()
        msgReference.setInterface(EaseMobMessageService.class);
        msgReference.setVersion("1.0.0");

        System.out.println("DubboConsumerHelper init end");
    }

    public static EaseMobMessageService getMsgService() {
        return msgReference.get();
    }

    public static void main(String[] args) {
        init();
        getMsgService().sendCmdMsg(null, null, null, null, null, null, 0, 0);
        getMsgService().sendTxtMsg(null, null, null, null, null, 0, 0);
//
//        System.out.println(NetUtils.getLocalAddress());
    }

}
