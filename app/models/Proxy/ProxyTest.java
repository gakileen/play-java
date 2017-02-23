package models.Proxy;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by ac on 2017/1/24.
 */
public class ProxyTest {

    public static void demo() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class clazzProxy = Proxy.getProxyClass(ArrayList.class.getClassLoader(), ArrayList.class.getInterfaces());
        Class clazz2 = Proxy.getProxyClass(ArrayList.class.getClassLoader(), ArrayList.class.getInterfaces());
        System.out.println(clazzProxy.getName());
        System.out.println(clazz2.getName());


        System.out.println("代理类" + clazzProxy.getName() + "的父类：" + clazzProxy.getSuperclass().getName());


        Class[] clazzInterfaces = clazzProxy.getInterfaces();
        StringBuilder sbInterfaces = new StringBuilder();
        for (Class clazzInterface : clazzInterfaces) {
            sbInterfaces.append(clazzInterface.getName()).append(",");
        }
        sbInterfaces.deleteCharAt(sbInterfaces.length()-1);
        System.out.println("代理类" + clazzProxy.getName() + "所实现的接口：" + sbInterfaces);


        System.out.println("代理类" + clazzProxy.getName() + "的访问修饰符：" + clazzProxy.getModifiers());


        System.out.println("\n-------------打印代理类" + clazzProxy.getName() + "的构造方法列表-------------");
        Constructor[] constructors = clazzProxy.getConstructors();
        for (Constructor constructor : constructors) {
            System.out.println("访问修饰符：" + constructor.getModifiers());
            String name = constructor.getName();
            StringBuilder sb = new StringBuilder(name);
            sb.append('(');
            Class[] clazzParameters = constructor.getParameterTypes();
            for (Class clazzParameter : clazzParameters) {
                sb.append(clazzParameter.getName()).append(",");
            }
            if (clazzParameters != null && clazzParameters.length != 0) {
                sb.deleteCharAt(sb.length()-1);
            }
            sb.append(')');
            System.out.println(sb.toString());
        }



        System.out.println("\n-------------打印代理类" + clazzProxy.getName() + "的方法列表-------------");
        Method[] methods = clazzProxy.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            StringBuilder sb = new StringBuilder(name);
            sb.append('(');
            Class[] clazzParameters = method.getParameterTypes();
            for (Class clazzParameter : clazzParameters) {
                sb.append(clazzParameter.getName()).append(",");
            }
            if (clazzParameters != null && clazzParameters.length != 0) {
                sb.deleteCharAt(sb.length()-1);
            }
            sb.append(')');
            System.out.println(sb.toString());
        }

        System.out.println("\n\n-----------");


        //由上述信息可推断出代理类的文件结构（程序中只实现打印代理类的声明部份）
        StringBuilder sbProxyClassStruct = new StringBuilder("public final class ");
        sbProxyClassStruct.append(clazzProxy.getName())
                .append(" extends ").append(clazzProxy.getSuperclass().getName())
                .append(" implements ").append(sbInterfaces).append(" { } ");
        System.out.println("代理类的结构：" + sbProxyClassStruct);








        //方式1，创建一个内部类，并实现InvocationHandler接口
        Constructor proxy1 = clazzProxy.getConstructor(InvocationHandler.class);
        System.out.println("------------创建代理类实例，方式1--------------------");
        class MyInvocationHandler implements InvocationHandler {
            ArrayList target = new ArrayList();
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                System.out.println(name + "方法调用前...");
                Object retVal = method.invoke(target, args);
                System.out.println(name + "方法调用后的返回结果为：" + retVal);
                System.out.println(name + "方法调用后...\n");
                return retVal;
            }

        }
        List list1 = (List)proxy1.newInstance(new MyInvocationHandler());
        list1.add("zhangsan");
        list1.add("lisi");
        System.out.println(list1.size());


        //方式3(匿名类)，将创建代理类和创建代理类实例对象的步聚合二为一
        System.out.println("------------创建代理类实例，方式3--------------------");
        List list3 = (List)Proxy.newProxyInstance(
                ArrayList.class.getClassLoader(),
                ArrayList.class.getInterfaces(),
                new InvocationHandler() {
                    ArrayList target = new ArrayList();
                    @Override
                    public Object invoke(Object proxy, Method method,
                                         Object[] args) throws Throwable {
                        //测试方法的运行效率
                        long beginTime = System.currentTimeMillis();
                        Object retVal = method.invoke(target, args);
                        long endTime = System.currentTimeMillis();
                        System.out.println(method.getName() + " 方法运行时长为：" + (endTime - beginTime));
                        return retVal;
                    }}
        );
        list3.add("hy");
        list3.add("yangxin");
        System.out.println(list3.size());
    }






    /**
     * 获得代理对象
     * @param target 目标（被代理的对象）
     * @param advice 目标对象中的方法被调用前要执行的功能
     * @return 目标的代理对象
     */
    private static Object getProxy(final Object target,final Advice advice) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    Object retVal = null;
                    try {
                        advice.doBefore(target, method, args);//方法执行前
                        retVal = method.invoke(target, args);
                        advice.doAfter(target, method, args, retVal);//方法执行后
                    } catch (Exception e) {
                        advice.doThrow(target, method, args, e);//方法抛出异常
                    } finally {
                        advice.doFinally(target, method, args);//方法最终执行代码(用于释放数据资源、关闭IO流等操作)
                    }
                    return retVal;
                }
        );
    }


    public static void main(String[] args) {
        ArrayList target = new ArrayList();
        List list4 = (List)getProxy(target,new LogAdvice());
        list4.add("张三");
        list4.add("李四");
        list4.add("王五");
        System.out.println(list4.size());
//        list4.get(3);   //演示异常advice
    }


}


/**
 * aop接口，提供方法运行前、方法运行后、方法运行中产生Exception、方法最终运行代码
 *
 */
interface Advice {

    /**
     * 方法运行前
     * @param target 被代理的目标对象
     * @param method 被调用的方法
     * @param args 方法的参数
     */
    public void doBefore(Object target, Method method, Object[] args);

    /**
     * 方法运行后
     * @param target 被代理的目标对象
     * @param method 被调用的方法对象
     * @param args 方法的参数
     * @param retVal 方法的返回值
     */
    public void doAfter(Object target, Method method, Object[] args, Object retVal);

    /**
     * 方法运行时产生的异常
     * @param target 被代理的目标对象
     * @param method 被调用的方法
     * @param args 方法参数
     * @param e 运行时的异常对象
     */
    public void doThrow(Object target, Method method, Object[] args, Exception e);

    /**
     * 最终要执行的功能(如释放数据库连接的资源、关闭IO流等)
     * @param target 被代理的目标对象
     * @param method 被调用的方法
     * @param args 方法参数
     */
    public void doFinally(Object target, Method method, Object[] args);
}



/**
 * 日志功能切入类
 * @author 杨信
 *
 */
class LogAdvice implements Advice {

    long beginTime = System.currentTimeMillis();

    @Override
    public void doBefore(Object target, Method method, Object[] args) {
        System.out.println(target.getClass().getSimpleName() +
                "." + method.getName() + "方法被调用，参数值：" + Arrays.toString(args));

    }

    @Override
    public void doAfter(Object target, Method method, Object[] args, Object retVal) {
        long endTime = System.currentTimeMillis();
        System.out.println(target.getClass().getSimpleName() +
                "." + method.getName() + "方法运行结束，返回值：" + retVal + "，耗时" + (endTime - beginTime) + "毫秒。");

    }

    @Override
    public void doThrow(Object target, Method method, Object[] args,
                        Exception e) {
        System.out.println("调用" + target.getClass().getSimpleName() +
                "." + method.getName() + "方法发生异常，异常消息：");
        e.printStackTrace();
    }

    @Override
    public void doFinally(Object target, Method method, Object[] args) {
        System.out.println("doFinally...");

    }

}